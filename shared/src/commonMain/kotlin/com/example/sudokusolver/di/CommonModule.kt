package com.example.sudokusolver.di

import com.example.sudokusolver.sudoku.SudokuViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val commonModule = module {
    viewModel { SudokuViewModel(sudokuRecognizer = getOrNull()) }
}


