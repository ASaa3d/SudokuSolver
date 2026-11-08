package com.example.sudokusolver.splash.data.remote

import com.example.sudokusolver.splash.data.remote.dto.UserDto

interface SplashRemoteDataSource {
    suspend fun ping(): Unit
    suspend fun getUserMe(token: String): UserDto
}
