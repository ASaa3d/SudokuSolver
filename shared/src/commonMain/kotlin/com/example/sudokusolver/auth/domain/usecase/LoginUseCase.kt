package com.example.sudokusolver.auth.domain.usecase

import com.example.sudokusolver.auth.domain.model.LoginData
import com.example.sudokusolver.auth.domain.repository.AuthRepository
import core.InputError
import core.Validators

class LoginUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(loginData: LoginData): Result<Unit> {
        return when {
            !Validators.isEmail(loginData.email) -> Result.failure(InputError.InvalidEmail)
            !Validators.isPassword(loginData.password) -> Result.failure(InputError.InvalidPassword)
            else -> repo.login(loginData = loginData)
        }
    }
}
