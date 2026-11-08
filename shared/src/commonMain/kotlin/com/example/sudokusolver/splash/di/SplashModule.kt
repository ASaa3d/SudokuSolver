package com.example.sudokusolver.splash.di

import com.example.sudokusolver.splash.data.remote.SplashRemoteDataSource
import com.example.sudokusolver.splash.data.remote.SplashRemoteDataSourceImpl
import com.example.sudokusolver.splash.data.repository.SplashRepositoryImpl
import com.example.sudokusolver.splash.domain.repository.SplashRepository
import com.example.sudokusolver.splash.domain.usecase.RefreshUseCase
import com.example.sudokusolver.splash.presentation.viewmodel.SplashViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val splashModule = module {
    singleOf(::SplashRemoteDataSourceImpl) bind SplashRemoteDataSource::class
    singleOf(::SplashRepositoryImpl) bind SplashRepository::class
    factoryOf(::RefreshUseCase)
    viewModelOf(::SplashViewModel)
}
