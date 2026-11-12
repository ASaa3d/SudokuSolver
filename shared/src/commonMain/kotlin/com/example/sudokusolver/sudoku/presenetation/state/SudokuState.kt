package com.example.sudokusolver.sudoku.presenetation.state

import com.example.sudokusolver.splash.data.remote.dto.UserDto
import com.example.sudokusolver.sudoku.domain.model.SudokuAction

data class SudokuState(
    val userInfo: UserDto?=null,
    val hintText: String="",
    val badNumbers: List<Pair<Int, Int>>? = null,
    val hintPosition: Pair<Int, Int>?=null,
    val selectedPosition: Pair<Int, Int>?=null,
    val gridSize: Int = -1,
    val sudokuGrid: List<List<Int>>? = null,
    val oldGrid: List<List<Int>>? = null,
    val actionStack: List<SudokuAction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
