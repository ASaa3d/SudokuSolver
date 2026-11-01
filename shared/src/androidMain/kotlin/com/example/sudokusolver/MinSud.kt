package com.example.sudokusolver

import android.content.Context
import android.graphics.Bitmap
import com.example.sudokusolver.sudoku.PixelFormat
import java.io.Closeable
import java.io.File


/**
 * Sudoku recognizer. Models are loaded once in the constructor; reuse the instance.
 * Not safe to call close() while another thread is inside process*().
 */
class MinSud(digitModelPath: String, yoloModelPath: String) : Closeable {

    private var handle: Long = nativeCreate(digitModelPath, yoloModelPath)

    init {
        require(handle != 0L) { "Failed to load models: $digitModelPath / $yoloModelPath" }
    }

    /** Returns an NxN board (N = 9 or 6, 0 = empty cell), or null if no grid was found. */
    fun process(bitmap: Bitmap): Array<IntArray>? {
        require(bitmap.config == Bitmap.Config.ARGB_8888) { "Bitmap must be ARGB_8888" }
        return nativeProcessBitmap(handle, bitmap)
    }

    fun process(imagePath: String): Array<IntArray>? = nativeProcessFile(handle, imagePath)

    fun process(
        data: ByteArray, width: Int, height: Int,
        format: PixelFormat, rowStride: Int = 0
    ): Array<IntArray>? = nativeProcessBytes(handle, data, width, height, format.code, rowStride)

    override fun close() {
        if (handle != 0L) {
            nativeDestroy(handle)
            handle = 0L
        }
    }

    private external fun nativeCreate(digitModelPath: String, yoloModelPath: String): Long
    private external fun nativeDestroy(handle: Long)
    private external fun nativeProcessBitmap(handle: Long, bitmap: Bitmap): Array<IntArray>?
    private external fun nativeProcessFile(handle: Long, path: String): Array<IntArray>?
    private external fun nativeProcessBytes(
        handle: Long, data: ByteArray, width: Int, height: Int, format: Int, rowStride: Int
    ): Array<IntArray>?

    companion object {
        init { System.loadLibrary("min_sud") }
    }
}


fun copyAsset(ctx: Context, name: String): String {
    val out = File(ctx.filesDir, name)
    val size = ctx.assets.openFd(name).use { it.length }   // needs noCompress, see below
    if (!out.exists() || out.length() != size) {
        ctx.assets.open(name).use { i -> out.outputStream().use { o -> i.copyTo(o) } }
    }
    return out.absolutePath
}