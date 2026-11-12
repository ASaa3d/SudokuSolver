package com.example.sudokusolver.sudoku.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

actual fun ImageBitmap.toByteArray(quality: Int): ByteArray {
    val stream = ByteArrayOutputStream()
    asAndroidBitmap().compress(android.graphics.Bitmap.CompressFormat.JPEG, quality.coerceIn(0, 100), stream)
    return stream.toByteArray()
}
