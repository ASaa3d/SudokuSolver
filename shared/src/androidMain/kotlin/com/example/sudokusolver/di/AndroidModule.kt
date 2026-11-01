package com.example.sudokusolver.di

import com.example.sudokusolver.AndroidSudokuRecognizer
import com.example.sudokusolver.sudoku.SudokuRecognizer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidModule = module {

    single<SudokuRecognizer> {
        AndroidSudokuRecognizer(androidContext())
    }


}