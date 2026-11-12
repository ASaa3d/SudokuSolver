package com.example.sudokusolver.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * A shape described as a radial function: given an angle in radians (0..2π),
 * returns the relative distance from the center to the outline.
 * Any radial function works, so you can add your own shapes.
 */
class LoadingShape(val name: String, val radiusAt: (angle: Float) -> Float) {
    companion object {
        private const val TAU = (2 * PI).toFloat()

        private fun polygon(sides: Int): (Float) -> Float = { a ->
            val step = TAU / sides
            val m = ((a % step) + step) % step
            (cos(PI / sides) / cos(m - step / 2)).toFloat()
        }

        private fun superellipse(a: Float, b: Float, k: Float): (Float) -> Float = { t ->
            (abs(cos(t) / a).pow(k) + abs(sin(t) / b).pow(k)).pow(-1f / k)
        }

        val SoftBurst = LoadingShape("SoftBurst") { 1f + 0.13f * cos(10f * it) }
        val Pentagon = LoadingShape("Pentagon", polygon(5))
        val Pill = LoadingShape("Pill", superellipse(1.2f, 0.62f, 3f))
        val Sunny = LoadingShape("Sunny") { 1f + 0.17f * cos(8f * it) }
        val Cookie = LoadingShape("Cookie") { 1f + 0.09f * cos(9f * it) }
        val Clover = LoadingShape("Clover") { 1f + 0.26f * cos(4f * it) }
        val Oval = LoadingShape("Oval", superellipse(1.15f, 0.8f, 2f))
        val Triangle = LoadingShape("Triangle", polygon(3))

        // 8 shapes x 135° per morph = 1080° = 3 full turns, so the loop is seamless.
        val Default = listOf(SoftBurst, Pentagon, Pill, Sunny, Cookie, Clover, Oval, Triangle)
    }
}

private const val SAMPLES = 180
private const val STEP_ROTATION = 135f

/** Samples a shape, rounds its corners a little, and normalizes it to RMS radius 1 so all shapes have a similar size. */
private fun profile(shape: LoadingShape): FloatArray {
    val raw = FloatArray(SAMPLES) { shape.radiusAt(it * (2 * PI).toFloat() / SAMPLES) }
    val window = if (shape.name in setOf("Pill", "Oval", "Clover", "SoftBurst")) 1 else 5
    val smooth = FloatArray(SAMPLES) { i ->
        var s = 0f
        for (j in -window..window) s += raw[(i + j + SAMPLES) % SAMPLES]
        s / (2 * window + 1)
    }
    var ms = 0f
    smooth.forEach { ms += it * it }
    val rms = sqrt(ms / SAMPLES)
    return FloatArray(SAMPLES) { smooth[it] / rms }
}

/** Ease-out with overshoot, gives the morph its springy expressive feel. */
private fun easeOutBack(t: Float): Float {
    val c1 = 1.9f
    val c3 = c1 + 1f
    val x = t - 1f
    return 1f + c3 * x * x * x + c1 * x * x
}

/**
 * Material 3 Expressive style loading indicator: a single shape that keeps
 * rotating while it morphs between different shapes.
 */
@Composable
fun MorphingLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    size: Dp = 48.dp,
    shapes: List<LoadingShape> = listOf(
        LoadingShape.Cookie,
        LoadingShape.Triangle,
        LoadingShape.Pentagon
    ),
    morphDurationMillis: Int = 650,
) {
    require(shapes.size >= 2) { "Need at least two shapes to morph between" }

    val profiles = remember(shapes) { shapes.map(::profile) }
    val path = remember { Path() }
    val count = shapes.size

    val transition = rememberInfiniteTransition(label = "morphingLoader")
    // 0 until count; the integer part is the current shape, the fraction is the morph progress.
    val progress = transition.animateFloat(
        initialValue = 0f,
        targetValue = count.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = morphDurationMillis * count, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "progress",
    )

    Canvas(
        modifier = modifier
            .size(size)
            .semantics { progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate },
    ) {
        // State is read inside the draw scope, so only drawing is invalidated each frame.
        val p = progress.value
        val index = floor(p).toInt()
        val fraction = p - index
        val e = easeOutBack(min(1f, fraction * 1.15f))

        val from = profiles[index % count]
        val to = profiles[(index + 1) % count]
        val radius = this.size.minDimension / 2f * 0.72f
        val cx = this.size.width / 2f
        val cy = this.size.height / 2f
        val twoPi = (2 * PI).toFloat()

        path.rewind()
        for (k in 0 until SAMPLES) {
            val r = (from[k] + (to[k] - from[k]) * e) * radius
            val a = k * twoPi / SAMPLES
            val x = cx + r * cos(a)
            val y = cy + r * sin(a)
            if (k == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()

        val rotation = (index + e) * STEP_ROTATION + p / count * 360f
        rotate(degrees = rotation) { drawPath(path, color) }
    }
}

/** Same indicator, drawn on top of a circular container. */
@Composable
fun ContainedMorphingLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    size: Dp = 48.dp,
    shapes: List<LoadingShape> = listOf(
        LoadingShape.Cookie,
        LoadingShape.Triangle,
        LoadingShape.Pentagon
    ),
    morphDurationMillis: Int = 650,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(containerColor, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        MorphingLoadingIndicator(
            color = color,
            size = size * 0.52f,
            shapes = shapes,
            morphDurationMillis = morphDurationMillis,
        )
    }
}