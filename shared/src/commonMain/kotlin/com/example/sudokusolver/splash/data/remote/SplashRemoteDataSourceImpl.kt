package com.example.sudokusolver.splash.data.remote

import com.example.sudokusolver.splash.data.remote.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class SplashRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : SplashRemoteDataSource {
    override suspend fun ping() {
        httpClient.get("/auth/ping")
    }

    override suspend fun getUserMe(token: String): UserDto {
        return httpClient.get("/api/users/me") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }
}
