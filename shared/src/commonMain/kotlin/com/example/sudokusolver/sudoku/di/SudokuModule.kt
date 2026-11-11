package com.example.sudokusolver.sudoku.di

import com.example.sudokusolver.sudoku.data.remote.SudokuRemoteDataSource
import com.example.sudokusolver.sudoku.data.remote.SudokuRemoteDataSourceImpl
import com.example.sudokusolver.sudoku.data.repository.SudokuRepositoryImpl
import com.example.sudokusolver.sudoku.domain.repository.SudokuRepository
import com.example.sudokusolver.sudoku.domain.usecase.GetSudokuGridUseCase
import com.example.sudokusolver.sudoku.domain.usecase.UploadImageUseCase
import com.example.sudokusolver.sudoku.SudokuViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val sudokuModule = module {
    singleOf(::SudokuRemoteDataSourceImpl) bind SudokuRemoteDataSource::class
    singleOf(::SudokuRepositoryImpl) bind SudokuRepository::class
    factoryOf(::UploadImageUseCase)
    factoryOf(::GetSudokuGridUseCase)
    viewModel { SudokuViewModel(sudokuRecognizer = getOrNull()) }
}
