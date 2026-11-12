package com.example.sudokusolver

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.example.sudokusolver.sudoku.domain.model.PixelFormat
import com.example.sudokusolver.sudoku.data.local.SudokuRecognizer

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
    override fun process(bitmap: ImageBitmap): Array<IntArray>? = native.process(bitmap.asAndroidBitmap())

    override fun close() = native.close()
}