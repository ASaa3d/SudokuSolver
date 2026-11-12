package com.example.sudokusolver.sudoku.domain.model

data class Hint(val row: Int, val col: Int, val value: Int, val reason: String)
