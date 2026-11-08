package com.example.sudokusolver.splash.domain.repository

interface SplashRepository {
    suspend fun refresh(): Result<String?>
}
