package com.example.sudokusolver.auth.domain.usecase

import com.example.sudokusolver.auth.domain.model.SignUpData
import com.example.sudokusolver.auth.domain.repository.AuthRepository
import core.InputError
import core.Validators

class SignUpUseCase(private val repo: AuthRepository) {

    suspend operator fun invoke(data: SignUpData): Result<Int> {
        return when {
            !Validators.isUsername(data.userName) -> Result.failure(InputError.InvalidUserName)
            !Validators.isEmail(data.email) -> Result.failure(InputError.InvalidEmail)
            !Validators.isPassword(data.password) -> Result.failure(InputError.InvalidPassword)
            else -> repo.signUp(data)
        }
    }
}
