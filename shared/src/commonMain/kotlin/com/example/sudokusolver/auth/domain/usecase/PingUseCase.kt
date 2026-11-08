package com.example.sudokusolver.auth.domain.usecase

import com.example.sudokusolver.auth.domain.repository.AuthRepository

class PingUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repo.ping()
    }
}
