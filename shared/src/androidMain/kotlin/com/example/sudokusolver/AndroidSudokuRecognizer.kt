package com.example.sudokusolver

import android.content.Context
import com.example.sudokusolver.sudoku.PixelFormat
import com.example.sudokusolver.sudoku.SudokuRecognizer

class AndroidSudokuRecognizer(context: Context) : SudokuRecognizer {

    private val native = MinSud(
        copyAsset(context, "cnn_dig.onnx"),
        copyAsset(context, "yolo_sud.onnx"),
    )

    override fun process(
        data: ByteArray, width: Int, height: Int,
        format: PixelFormat, rowStride: Int,
    ): Array<IntArray>? = native.process(data, width, height, format, rowStride)

    override fun processFile(
        path: String
    ): Array<IntArray>? = native.process(path)

    /** Android-only shortcut for when you already have a Bitmap. */
    fun process(bitmap: android.graphics.Bitmap): Array<IntArray>? = native.process(bitmap)

    override fun close() = native.close()
}