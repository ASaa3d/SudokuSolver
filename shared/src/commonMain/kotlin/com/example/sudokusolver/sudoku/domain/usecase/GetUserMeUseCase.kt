package com.example.sudokusolver.sudoku.domain.usecase

import com.example.sudokusolver.splash.data.remote.dto.UserDto
import com.example.sudokusolver.sudoku.domain.repository.SudokuRepository

class GetUserMeUseCase(
    private val repository: SudokuRepository,
) {
    suspend operator fun invoke(): Result<UserDto> = repository.getUserMe()
}
