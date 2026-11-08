package com.example.sudokusolver.auth.domain.usecase

import com.example.sudokusolver.auth.domain.repository.AuthRepository

class ResendVerificationUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(userId: Int): Result<Unit> {
        return repo.resendVerification(userId = userId)
    }
}
