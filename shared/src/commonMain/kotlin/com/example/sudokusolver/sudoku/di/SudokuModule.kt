package com.example.sudokusolver.sudoku.di

import com.example.sudokusolver.sudoku.data.remote.SudokuRemoteDataSource
import com.example.sudokusolver.sudoku.data.remote.SudokuRemoteDataSourceImpl
import com.example.sudokusolver.sudoku.data.repository.SudokuRepositoryImpl
import com.example.sudokusolver.sudoku.domain.repository.SudokuRepository
import com.example.sudokusolver.sudoku.domain.usecase.GetSudokuGridMobileUseCase
import com.example.sudokusolver.sudoku.domain.usecase.GetSudokuGridWebUseCase
import com.example.sudokusolver.sudoku.domain.usecase.GetUserMeUseCase
import com.example.sudokusolver.sudoku.domain.usecase.UpdateProfilePhotoUseCase
import com.example.sudokusolver.sudoku.domain.usecase.SignOutUseCase
import com.example.sudokusolver.sudoku.presenetation.viewmodel.SudokuViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sudokuModule = module {
    singleOf(::SudokuRemoteDataSourceImpl) bind SudokuRemoteDataSource::class
    single<SudokuRepository> {
        SudokuRepositoryImpl(
            remoteDataSource = get(),
            sudokuRecognizer = getOrNull(),
            splashRemoteDataSource = get(),
            localDataSource = get(),
        )
    }
    factoryOf(::GetSudokuGridMobileUseCase)
    factoryOf(::GetSudokuGridWebUseCase)
    factoryOf(::GetUserMeUseCase)
    factoryOf(::UpdateProfilePhotoUseCase)
    factoryOf(::SignOutUseCase)
    viewModelOf(::SudokuViewModel)
}
