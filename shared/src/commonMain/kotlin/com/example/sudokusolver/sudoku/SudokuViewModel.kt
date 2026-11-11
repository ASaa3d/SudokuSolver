package com.example.sudokusolver.sudoku

import androidx.lifecycle.ViewModel
import com.example.sudokusolver.sudoku.data.local.SudokuRecognizer


class SudokuViewModel(
    private val sudokuRecognizer : SudokuRecognizer?
) : ViewModel() {
    val recState = (sudokuRecognizer != null)


}