package com.example.sudokusolver.sudoku.domain.usecase

import com.example.sudokusolver.sudoku.domain.repository.SudokuRepository

class UpdateProfilePhotoUseCase(
    private val repository: SudokuRepository,
) {
    suspend operator fun invoke(imageByteArray: ByteArray): Result<String> =
        repository.updateProfilePhoto(imageByteArray)
}
