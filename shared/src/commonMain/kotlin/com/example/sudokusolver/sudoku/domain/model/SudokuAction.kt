package com.example.sudokusolver.sudoku.domain.model

sealed class SudokuAction {
    data class ADD(val x:Int , val y:Int, val value:Int) : SudokuAction()
    data class REMOVE(val x:Int , val y:Int, val value:Int) : SudokuAction()
    data object SOLVE : SudokuAction()
}