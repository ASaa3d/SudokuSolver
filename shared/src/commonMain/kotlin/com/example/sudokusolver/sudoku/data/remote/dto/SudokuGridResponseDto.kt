package com.example.sudokusolver.sudoku.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SudokuGridResponseDto(
    val grid: List<List<Int>>,
)
