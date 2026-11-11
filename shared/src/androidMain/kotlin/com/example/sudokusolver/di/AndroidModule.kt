package com.example.sudokusolver.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.sudokusolver.AndroidSudokuRecognizer
import com.example.sudokusolver.createDataStore
import com.example.sudokusolver.sudoku.data.local.SudokuRecognizer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidModule = module {

    single<SudokuRecognizer> {
        AndroidSudokuRecognizer(androidContext())
    }
    single<DataStore<Preferences>> { createDataStore(androidContext()) }

}