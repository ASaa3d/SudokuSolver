package com.example.sudokusolver.auth.di

import com.example.sudokusolver.auth.data.remote.AuthRemoteDataSource
import com.example.sudokusolver.auth.data.remote.AuthRemoteDataSourceImpl
import com.example.sudokusolver.auth.data.repository.AuthRepositoryImpl
import com.example.sudokusolver.auth.domain.repository.AuthRepository
import com.example.sudokusolver.auth.domain.usecase.ResetPasswordEmailUseCase
import com.example.sudokusolver.auth.domain.usecase.GetTokenUseCase
import com.example.sudokusolver.auth.domain.usecase.LoginUseCase
import com.example.sudokusolver.auth.domain.usecase.PingUseCase
import com.example.sudokusolver.auth.domain.usecase.ResendVerificationUseCase
import com.example.sudokusolver.auth.domain.usecase.ResetPasswordUseCase
import com.example.sudokusolver.auth.domain.usecase.SignUpUseCase
import com.example.sudokusolver.auth.domain.usecase.ResetPasswordCodeUseCase
import com.example.sudokusolver.auth.domain.usecase.VerifyAccountUseCase
import com.example.sudokusolver.auth.presentation.viewmodel.LoginViewModel
import com.example.sudokusolver.auth.presentation.viewmodel.SignUpViewModel
import com.example.sudokusolver.auth.presentation.viewmodel.ResetPasswordEmailViewModel
import com.example.sudokusolver.auth.presentation.viewmodel.ResetPasswordCodeViewModel
import com.example.sudokusolver.auth.presentation.viewmodel.ResetPasswordViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    singleOf(::AuthRemoteDataSourceImpl) bind AuthRemoteDataSource::class
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::ResetPasswordEmailViewModel)
    viewModelOf(::ResetPasswordCodeViewModel)
    viewModelOf(::ResetPasswordViewModel)
    factoryOf(::LoginUseCase)
    factoryOf(::PingUseCase)
    factoryOf(::VerifyAccountUseCase)
    factoryOf(::SignUpUseCase)
    factoryOf(::ResetPasswordEmailUseCase)
    factoryOf(::ResetPasswordUseCase)
    factoryOf(::ResetPasswordCodeUseCase)
    factoryOf(::ResendVerificationUseCase)
    factoryOf(::GetTokenUseCase)
}
