package com.example.app.splash // TODO: change to your package

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/** Classic overshoot curve, equivalent to Android's OvershootInterpolator but pure Kotlin (KMP-safe). */
private fun overshootEasing(tension: Float = 2.5f) = Easing { t ->
    val x = t - 1f
    x * x * ((tension + 1f) * x + tension) + 1f
}

// 81 chars, row by row, '0' = empty cell. Replace with any puzzle you like.
private const val PUZZLE =
    "530070000" +
            "600195000" +
            "098000060" +
            "800060003" +
            "400803001" +
            "700020006" +
            "060000280" +
            "000419005" +
            "000080079"

// Thick (3x3 box) lines are drawn first, then the thin ones.
private val LINE_ORDER = intArrayOf(3, 6, 1, 2, 4, 5, 7, 8)

/**
 * Splash animation:
 * 1. Empty rounded square bounces in with overshoot
 * 2. Sudoku grid lines draw themselves inside it
 * 3. Digits pop in cell by cell
 * 4. Small final pulse, then [onFinished] is called
 */
@Composable
fun SudokuSplash(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF101418),
    frameColor: Color = Color(0xFFFFFFFF),
    gridColor: Color = Color(0xFF8FA3B8),
    numberColor: Color = Color(0xFF4FC3F7),
) {
    val currentOnFinished by rememberUpdatedState(onFinished)

    val logoScale = remember { Animatable(0f) }
    val gridProgress = remember { Animatable(0f) }
    val numbersProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // 1) Bounce in (empty square)
        logoScale.animateTo(1f, tween(durationMillis = 750, easing = overshootEasing()))
        // 2) Draw grid
        gridProgress.animateTo(1f, tween(durationMillis = 900, easing = FastOutSlowInEasing))
        // 3) Digits
        numbersProgress.animateTo(1f, tween(durationMillis = 1100, easing = LinearEasing))
        // 4) Final pulse
        logoScale.animateTo(1.06f, tween(100))
        logoScale.animateTo(
            1f,
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        )
        delay(250.milliseconds)
        currentOnFinished()
    }

    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier.fillMaxSize().background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        val side = minOf(maxWidth, maxHeight) * 0.7f
        val fontSize = with(density) { (side / 9f * 0.55f).toSp() }

        // Measure the nine digits once, not on every frame.
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
            val thickStroke = this.size.width * 0.014f
            val thinStroke = this.size.width * 0.006f
            val inset = frameStroke / 2f
            val gridSize = this.size.width - frameStroke
            val cell = gridSize / 9f
            val origin = Offset(inset, inset)

            // --- Grid lines (drawn under the frame) ---
            val stagger = 0.06f
            val span = 0.5f
            LINE_ORDER.forEachIndexed { rank, i ->
                val p = ((gridProgress.value - rank * stagger) / span).coerceIn(0f, 1f)
                if (p > 0f) {
                    val isThick = i % 3 == 0
                    val color = if (isThick) frameColor else gridColor
                    val width = if (isThick) thickStroke else thinStroke

                    // vertical line, grows top -> bottom
                    val x = origin.x + i * cell
                    drawLine(
                        color = color,
                        start = Offset(x, origin.y),
                        end = Offset(x, origin.y + gridSize * p),
                        strokeWidth = width,
                        cap = StrokeCap.Round,
                    )
                    // horizontal line, grows left -> right
                    val y = origin.y + i * cell
                    drawLine(
                        color = color,
                        start = Offset(origin.x, y),
                        end = Offset(origin.x + gridSize * p, y),
                        strokeWidth = width,
                        cap = StrokeCap.Round,
                    )
                }
            }

            // --- Digits ---
            val np = numbersProgress.value
            if (np > 0f) {
                for (index in 0 until 81) {
                    val ch = PUZZLE[index]
                    if (ch == '0') continue
                    val digit = ch - '0'

                    // Deterministic pseudo-random reveal order (37 is coprime with 81 -> a permutation).
                    val rank = (index * 37) % 81
                    val p = (np * 1.5f - (rank / 81f) * 0.5f).coerceIn(0f, 1f)
                    if (p <= 0f) continue

                    val layout = digitLayouts[digit - 1]
                    val row = index / 9
                    val col = index % 9
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

            // --- Outer frame (always visible: the "empty square" that bounces in) ---
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