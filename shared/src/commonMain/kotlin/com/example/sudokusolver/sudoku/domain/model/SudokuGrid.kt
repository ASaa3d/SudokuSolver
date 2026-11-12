package com.example.sudokusolver.sudoku.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SudokuGrid(
    val grid: List<List<Int>>,
    val gridSize: Int,
)
