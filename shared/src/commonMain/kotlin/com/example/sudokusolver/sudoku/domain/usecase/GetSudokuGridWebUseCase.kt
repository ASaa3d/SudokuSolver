package com.example.sudokusolver.sudoku.domain.usecase

import com.example.sudokusolver.sudoku.domain.model.SudokuGrid
import com.example.sudokusolver.sudoku.domain.repository.SudokuRepository

class GetSudokuGridWebUseCase(
    private val repository: SudokuRepository,
) {
    suspend operator fun invoke(imageByteArray: ByteArray): Result<SudokuGrid> =
        repository.getSudokuGridFromWeb(imageByteArray)
}
