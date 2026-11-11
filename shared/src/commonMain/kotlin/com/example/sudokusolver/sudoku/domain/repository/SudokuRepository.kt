package com.example.sudokusolver.sudoku.domain.repository

import com.example.sudokusolver.sudoku.domain.model.ImgBbData
import com.example.sudokusolver.sudoku.domain.model.SudokuGrid

interface SudokuRepository {
    suspend fun uploadImageToImgBb(
        imageBytes: ByteArray,
        apiKey: String,
        fileName: String = "image.jpg",
    ): Result<ImgBbData>

    suspend fun getSudokuGrid(imageUrl: String): Result<SudokuGrid>
}
