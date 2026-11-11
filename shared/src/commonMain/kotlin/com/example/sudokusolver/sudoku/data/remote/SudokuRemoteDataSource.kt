package com.example.sudokusolver.sudoku.data.remote

import com.example.sudokusolver.sudoku.data.remote.dto.ImgBbResponseDto
import com.example.sudokusolver.sudoku.data.remote.dto.SudokuGridResponseDto

interface SudokuRemoteDataSource {
    suspend fun uploadImageToImgBb(
        imageBytes: ByteArray,
        apiKey: String,
        fileName: String = "image.jpg",
    ): ImgBbResponseDto

    suspend fun getSudokuGrid(imageUrl: String): SudokuGridResponseDto
}
