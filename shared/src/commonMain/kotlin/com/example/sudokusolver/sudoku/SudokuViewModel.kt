package com.example.sudokusolver.sudoku

import androidx.lifecycle.ViewModel


class SudokuViewModel(
    private val sudokuRecognizer : SudokuRecognizer?
) : ViewModel() {
    val recState = (sudokuRecognizer != null)

}