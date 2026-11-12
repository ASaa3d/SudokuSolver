package com.example.sudokusolver.sudoku.util

import androidx.compose.ui.graphics.ImageBitmap

expect fun ImageBitmap.toByteArray(quality: Int = 100): ByteArray
