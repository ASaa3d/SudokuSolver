package com.example.sudokusolver.splash.domain.usecase

import com.example.sudokusolver.splash.domain.repository.SplashRepository

class RefreshUseCase(
    private val repository: SplashRepository
) {
    suspend operator fun invoke(): Result<String?> {
        return repository.refresh()
    }
}
