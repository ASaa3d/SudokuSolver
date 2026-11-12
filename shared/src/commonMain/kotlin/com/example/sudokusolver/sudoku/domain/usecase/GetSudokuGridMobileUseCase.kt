package com.example.sudokusolver.sudoku.domain.usecase

import androidx.compose.ui.graphics.ImageBitmap
import com.example.sudokusolver.sudoku.domain.model.SudokuGrid
import com.example.sudokusolver.sudoku.domain.repository.SudokuRepository

class GetSudokuGridMobileUseCase(
    private val repository: SudokuRepository,
) {
    suspend operator fun invoke(imageBitmap: ImageBitmap): Result<SudokuGrid> =
        repository.getSudokuGridFromMobile(imageBitmap)
}
