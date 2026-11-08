package com.example.sudokusolver.auth.domain.usecase

import com.example.sudokusolver.auth.domain.repository.AuthRepository

class GetTokenUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(): Result<String> {
        return repo.getToken()
    }
}
