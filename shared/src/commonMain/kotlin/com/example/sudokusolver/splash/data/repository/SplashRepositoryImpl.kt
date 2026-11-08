package com.example.sudokusolver.splash.data.repository

import com.example.sudokusolver.datastore.LocalDataSource
import com.example.sudokusolver.splash.data.remote.SplashRemoteDataSource
import com.example.sudokusolver.splash.domain.repository.SplashRepository

class SplashRepositoryImpl(
    private val remoteDataSource: SplashRemoteDataSource,
    private val localDataSource: LocalDataSource
) : SplashRepository {
    companion object {
        private const val TOKEN_KEY = "access_token"
    }

    override suspend fun refresh(): Result<String?> {
        val tokenResult = runCatching { localDataSource.get(TOKEN_KEY) }
        if (tokenResult.isSuccess) {
            val token = tokenResult.getOrNull()
            if (!token.isNullOrBlank()) {
                val userResult = runCatching { remoteDataSource.getUserMe(token) }
                if (userResult.isSuccess) {
                    val user = userResult.getOrNull()
                    val email = user?.email?.takeIf { it.isNotBlank() } ?: user?.userName ?: ""
                    return Result.success(email)
                } else {
                    return Result.failure(userResult.exceptionOrNull() ?: Exception("Failed to fetch user profile"))
                }
            }
        }
        // Token not found or error -> ping server to make sure it's alive, then go to login
        val pingResult = runCatching { remoteDataSource.ping() }
        return if (pingResult.isSuccess) {
            Result.failure(Exception("Token not found, server alive, redirect to login"))
        } else {
            Result.failure(pingResult.exceptionOrNull() ?: Exception("Server unreachable"))
        }
    }
}
