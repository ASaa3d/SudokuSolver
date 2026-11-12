package com.example.sudokusolver.sudoku.data.local

import androidx.compose.ui.graphics.ImageBitmap
import com.example.sudokusolver.sudoku.domain.model.PixelFormat

interface SudokuRecognizer : AutoCloseable {

    /**
     * @return an NxN board (N = 9 or 6, 0 = empty cell), or null if no grid was found.
     * Blocking call: run it off the main thread.
     */
    fun process(
        data: ByteArray,
        width: Int,
        height: Int,
        format: PixelFormat,
        rowStride: Int = 0,
    ): Array<IntArray>?
    fun processFile(
        path: String
    ): Array<IntArray>?

    fun process(bitmap: ImageBitmap): Array<IntArray>?
}