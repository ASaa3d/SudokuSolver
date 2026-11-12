package com.example.sudokusolver.sudoku.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.toPixelMap
import kotlin.io.encoding.Base64
import kotlinx.browser.document
import org.khronos.webgl.set
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement


actual fun ImageBitmap.toByteArray(quality: Int): ByteArray {
    val image = org.jetbrains.skia.Image.makeFromBitmap(asSkiaBitmap())
    val skiaFormat = org.jetbrains.skia.EncodedImageFormat.JPEG
    return image.encodeToData(skiaFormat, quality)?.bytes
        ?: error("Failed to encode image")
}
