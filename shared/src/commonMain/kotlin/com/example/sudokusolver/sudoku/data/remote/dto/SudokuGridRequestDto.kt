package com.example.sudokusolver.sudoku.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SudokuGridRequestDto(
    val imageUrl: String,
)
