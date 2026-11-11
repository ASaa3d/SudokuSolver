package com.example.sudokusolver.sudoku.domain.usecase

import com.example.sudokusolver.sudoku.domain.model.SudokuGrid
import com.example.sudokusolver.sudoku.domain.repository.SudokuRepository

class GetSudokuGridUseCase(
    private val repository: SudokuRepository,
) {
    suspend operator fun invoke(imageUrl: String): Result<SudokuGrid> {
        if (imageUrl.isBlank()) {
            return Result.failure(IllegalArgumentException("Image URL cannot be blank"))
        }
        return repository.getSudokuGrid(imageUrl)
    }
}
