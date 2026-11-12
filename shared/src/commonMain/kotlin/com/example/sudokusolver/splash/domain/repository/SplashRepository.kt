package com.example.sudokusolver.splash.domain.repository

import com.example.sudokusolver.splash.data.remote.dto.UserDto

interface SplashRepository {
    suspend fun refresh(): Result<String?>
}
