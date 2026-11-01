package com.example.sudokusolver.sudoku

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sudokusolver.di.getKoin


class SudokuViewModel(
    private val sudokuRecognizer : SudokuRecognizer? = null
) : ViewModel() {
    val recState = (sudokuRecognizer != null)

}