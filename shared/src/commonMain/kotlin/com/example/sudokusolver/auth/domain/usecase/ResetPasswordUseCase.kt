package com.example.sudokusolver.auth.domain.usecase

import com.example.sudokusolver.auth.domain.repository.AuthRepository
import core.InputError
import core.Validators

class ResetPasswordUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String, newPassword: String): Result<Unit> {
        return when {
            !Validators.isEmail(email) -> Result.failure(InputError.InvalidEmail)
            !Validators.isPassword(newPassword) -> Result.failure(InputError.InvalidPassword)
            else -> repo.resetPassword(email = email, newPassword = newPassword)
        }
    }
}
