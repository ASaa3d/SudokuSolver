package com.example.sudokusolver.sudoku.domain.usecase

import com.example.sudokusolver.sudoku.domain.repository.SudokuRepository

class SignOutUseCase(
    private val sudokuRepository: SudokuRepository
) {
     suspend operator fun invoke():Result<Unit> {
         return sudokuRepository.signOut()
     }
}