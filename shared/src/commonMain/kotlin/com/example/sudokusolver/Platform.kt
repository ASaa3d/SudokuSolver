package com.example.sudokusolver

import com.example.sudokusolver.sudoku.SudokuRecognizer


interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val showSplash: Boolean