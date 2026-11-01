package com.example.sudokusolver

import android.os.Build
import com.example.sudokusolver.sudoku.SudokuRecognizer

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val showSplash: Boolean = true