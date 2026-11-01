package com.example.sudokusolver

import android.content.Context

class AndroidSudokuRecognizer(context: Context) : SudokuRecognizer {

    private val native = MinSud(
        copyAsset(context, "cnn_dig.onnx"),
        copyAsset(context, "yolo_sud.onnx"),
    )

    override fun process(
        data: ByteArray, width: Int, height: Int,
        format: PixelFormat, rowStride: Int,
    ): Array<IntArray>? = native.process(data, width, height, format, rowStride)

    /** Android-only shortcut for when you already have a Bitmap. */
    fun process(bitmap: android.graphics.Bitmap): Array<IntArray>? = native.process(bitmap)

    override fun close() = native.close()
}