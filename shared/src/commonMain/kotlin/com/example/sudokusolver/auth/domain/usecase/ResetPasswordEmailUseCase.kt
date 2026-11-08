package com.example.sudokusolver.auth.domain.usecase

import com.example.sudokusolver.auth.domain.repository.AuthRepository
import core.InputError
import core.Validators

class ResetPasswordEmailUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return when {
            !Validators.isEmail(email) -> Result.failure(InputError.InvalidEmail)
            else -> repo.resetPasswordEmail(email = email)
        }
    }
}
