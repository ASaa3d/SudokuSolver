package com.example.sudokusolver.auth.domain.usecase

import com.example.sudokusolver.auth.domain.repository.AuthRepository
import core.InputError
import core.Validators

class ResendPasswordChangeUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (!Validators.isEmail(email)) {
            return Result.failure(InputError.InvalidEmail)
        }
        return repo.resendPasswordChange(email = email)
    }
}
