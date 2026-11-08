package com.example.sudokusolver.auth.domain.usecase

import com.example.sudokusolver.auth.domain.repository.AuthRepository
import core.InputError
import core.Validators

class VerifyAccountUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String, code: String): Result<Unit> {
        return when {
            !Validators.isEmail(email) -> Result.failure(InputError.InvalidEmail)
            !Validators.isCode(code) -> Result.failure(InputError.InvalidCode)
            else -> repo.verify(email = email, code = code)
        }
    }
}
