package com.example.sudokusolver.sudoku.domain.usecase

import com.example.sudokusolver.sudoku.domain.model.ImgBbData
import com.example.sudokusolver.sudoku.domain.repository.SudokuRepository

class UploadImageUseCase(
    private val repository: SudokuRepository,
) {
    suspend operator fun invoke(
        imageBytes: ByteArray,
        apiKey: String,
        fileName: String = "image.jpg",
    ): Result<ImgBbData> {
        if (imageBytes.isEmpty()) {
            return Result.failure(IllegalArgumentException("Image bytes cannot be empty"))
        }
        if (apiKey.isBlank()) {
            return Result.failure(IllegalArgumentException("API key cannot be blank"))
        }
        return repository.uploadImageToImgBb(imageBytes, apiKey, fileName)
    }
}
