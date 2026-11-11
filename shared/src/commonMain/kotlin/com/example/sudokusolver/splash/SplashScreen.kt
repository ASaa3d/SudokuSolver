package com.example.sudokusolver.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.sudokusolver.navigation.Screen
import com.example.sudokusolver.splash.presentation.event.SplashEvent
import com.example.sudokusolver.splash.presentation.viewmodel.SplashViewModel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import theme.SlateBlue
import theme.Taupe
import kotlin.time.Duration.Companion.milliseconds


/** Classic overshoot curve, equivalent to Android's OvershootInterpolator but pure Kotlin (KMP-safe). */
private fun overshootEasing(tension: Float = 2.5f) = Easing { t ->
    val x = t - 1f
    x * x * ((tension + 1f) * x + tension) + 1f
}

private const val N = 3 // single 3x3 box

// 9 chars, row by row, '0' = empty cell:
//  1 2 _
//  6 _ _
//  _ 4 5
private const val PUZZLE = "120600045"

@Composable
fun SplashScreen(
    navBackStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = koinViewModel(),
    target:String? = null
) {
    println("splash target=$target vm=${viewModel.hashCode()}")
    val state by viewModel.state.collectAsStateWithLifecycle()
    var animationCycle by remember { mutableStateOf(0) }
    var animationFinished by remember { mutableStateOf(false) }
    var pendingEvent by remember { mutableStateOf<SplashEvent?>(null) }



    fun navigate(event: SplashEvent) {

        when (event) {
            is SplashEvent.NavigateToHome -> {
                navBackStack.add(Screen.Home(event.email, event.isGuest))
            }
            is SplashEvent.NavigateToLogin -> {
                navBackStack.add(Screen.Login)
            }
            is SplashEvent.NavigateToSignUp -> {
                navBackStack.add(Screen.SignUp)
            }
            is SplashEvent.NavigateToVerifyAccount -> {
                navBackStack.add(Screen.VerifyAccount(event.email, event.code))
            }
            is SplashEvent.NavigateToResetAccountEmail -> {
                navBackStack.add(Screen.ResetAccountEmail)
            }
            is SplashEvent.NavigateToResetAccountCode -> {
                navBackStack.add(Screen.ResetAccountCode(event.email, event.isVerifyAccount))
            }
            is SplashEvent.NavigateToResetPassword -> {
                navBackStack.add(Screen.ResetPassword(event.email, event.code))
            }
            is SplashEvent.ResolveTarget -> {
                viewModel.resolveTarget(target = target, email = event.email)
            }
        }
    }


    LaunchedEffect(Unit) {
        println("pending event ${pendingEvent.toString()}")
        viewModel.refresh() }

    LaunchedEffect(Unit) {

        viewModel.events.collect { pendingEvent = it }
    }

    LaunchedEffect(pendingEvent, animationFinished, state.isLoading) {
        val event = pendingEvent
        if (event != null && animationFinished && !state.isLoading) {
            pendingEvent = null
            navigate(event)
        }
    }

    SplashScreenContent(
        modifier = modifier,
        animationKey = animationCycle,
        onFinished = {
            if (state.isLoading) {
                animationCycle++
            } else {
                animationFinished = true
            }
        }
    )
}


@Composable
fun SplashScreenContent(
    animationKey: Int,
    onFinished : ()->Unit,
    modifier: Modifier = Modifier,
) {
    val isDark = isSystemInDarkTheme()
    val colorScheme = MaterialTheme.colorScheme
    val backgroundColor = if (isDark) SlateBlue else colorScheme.background
    val frameColor = if (isDark) colorScheme.background else SlateBlue
    val gridColor = Taupe
    val numberColor = colorScheme.primary

    val logoScale = remember { Animatable(0f) }
    val gridProgress = remember { Animatable(0f) }
    val numbersProgress = remember { Animatable(0f) }

    LaunchedEffect(animationKey) {
        logoScale.snapTo(0f)
        gridProgress.snapTo(0f)
        numbersProgress.snapTo(0f)

        // 1) Bounce in (empty square)
        logoScale.animateTo(1f, tween(durationMillis = 750, easing = overshootEasing()))
        // 2) Draw grid
        gridProgress.animateTo(1f, tween(durationMillis = 700, easing = FastOutSlowInEasing))
        // 3) Digits
        numbersProgress.animateTo(1f, tween(durationMillis = 900, easing = LinearEasing))
        // 4) Final pulse
        logoScale.animateTo(1.06f, tween(100))
        logoScale.animateTo(
            1f,
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        )
        delay(250.milliseconds)
        onFinished()
    }

    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier.fillMaxSize().background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        val side = minOf(maxWidth, maxHeight) * 0.6f
        val fontSize = with(density) { (side / N * 0.6f).toSp() }

        // Measure the digits once, not on every frame.
        val digitLayouts = remember(fontSize, numberColor) {
            (1..9).map { d ->
                textMeasurer.measure(
                    text = AnnotatedString(d.toString()),
                    style = TextStyle(
                        color = numberColor,
                        fontSize = fontSize,
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
        }

        Canvas(
            modifier = Modifier
                .size(side)
                .graphicsLayer {
                    scaleX = logoScale.value
                    scaleY = logoScale.value
                },
        ) {
            val frameStroke = this.size.width * 0.028f
            val innerStroke = this.size.width * 0.016f
            val inset = frameStroke / 2f
            val gridSize = this.size.width - frameStroke
            val cell = gridSize / N
            val origin = Offset(inset, inset)

            // --- Inner grid lines (drawn under the frame) ---
            val stagger = 0.25f
            val span = 0.75f
            for (i in 1 until N) {
                val p = ((gridProgress.value - (i - 1) * stagger) / span).coerceIn(0f, 1f)
                if (p <= 0f) continue

                // vertical line, grows top -> bottom
                val x = origin.x + i * cell
                drawLine(
                    color = gridColor,
                    start = Offset(x, origin.y),
                    end = Offset(x, origin.y + gridSize * p),
                    strokeWidth = innerStroke,
                    cap = StrokeCap.Round,
                )
                // horizontal line, grows left -> right
                val y = origin.y + i * cell
                drawLine(
                    color = gridColor,
                    start = Offset(origin.x, y),
                    end = Offset(origin.x + gridSize * p, y),
                    strokeWidth = innerStroke,
                    cap = StrokeCap.Round,
                )
            }

            // --- Digits ---
            val np = numbersProgress.value
            if (np > 0f) {
                for (index in 0 until N * N) {
                    val ch = PUZZLE[index]
                    if (ch == '0') continue
                    val digit = ch - '0'

                    // Reveal in reading order.
                    val p = (np * 1.6f - (index / (N * N).toFloat()) * 0.6f).coerceIn(0f, 1f)
                    if (p <= 0f) continue

                    val layout = digitLayouts[digit - 1]
                    val row = index / N
                    val col = index % N
                    val center = Offset(
                        origin.x + (col + 0.5f) * cell,
                        origin.y + (row + 0.5f) * cell,
                    )
                    val popScale = 0.6f + 0.4f * p

                    scale(scale = popScale, pivot = center) {
                        drawText(
                            textLayoutResult = layout,
                            topLeft = Offset(
                                center.x - layout.size.width / 2f,
                                center.y - layout.size.height / 2f,
                            ),
                            alpha = p,
                        )
                    }
                }
            }

            // Outer frame
            drawRoundRect(
                color = frameColor,
                topLeft = origin,
                size = Size(gridSize, gridSize),
                cornerRadius = CornerRadius(this.size.width * 0.04f),
                style = Stroke(width = frameStroke),
            )
        }
    }
}
