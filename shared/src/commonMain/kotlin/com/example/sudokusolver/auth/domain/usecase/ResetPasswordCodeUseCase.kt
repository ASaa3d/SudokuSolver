package com.example.sudokusolver.auth.domain.usecase

import com.example.sudokusolver.auth.domain.repository.AuthRepository
import core.InputError
import core.Validators

class ResetPasswordCodeUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String, code: String): Result<Unit> {
        return when {
            !Validators.isEmail(email) -> Result.failure(InputError.InvalidEmail)
            !Validators.isCode(code) -> Result.failure(InputError.InvalidCode)
            else -> repo.resetPasswordCode(email = email, code = code)
        }
    }
}
