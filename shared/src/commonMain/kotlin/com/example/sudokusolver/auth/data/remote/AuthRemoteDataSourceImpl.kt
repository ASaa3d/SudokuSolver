package com.example.sudokusolver.auth.data.remote

import com.example.sudokusolver.auth.data.remote.dto.ForgetPasswordDto
import com.example.sudokusolver.auth.data.remote.dto.LoginResponseDto
import com.example.sudokusolver.auth.data.remote.dto.ResetPasswordDto
import com.example.sudokusolver.auth.data.remote.dto.SignUpResponseDto
import com.example.sudokusolver.auth.data.remote.dto.ValidateResetPasswordDto
import com.example.sudokusolver.auth.data.remote.dto.VerifyAccountDto
import com.example.sudokusolver.auth.domain.model.LoginData
import com.example.sudokusolver.auth.domain.model.SignUpData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthRemoteDataSourceImpl(
    private val httpClient : HttpClient,
) : AuthRemoteDataSource {



    override suspend fun login(loginData: LoginData): String {
        val loginResponse : LoginResponseDto= httpClient.post("/auth/login") {setBody(loginData)}.body()
        return loginResponse.token
    }

    override suspend fun verify(verifyDto: VerifyAccountDto) {
        httpClient.post("/auth/verify") {setBody(verifyDto)}
    }

    override suspend fun signUp(signUpData: SignUpData): Int {
        val response :SignUpResponseDto = httpClient.post("/auth/verify"){setBody(signUpData)}.body()
        return response.id
    }

    override suspend fun forgotPassword(forgetPasswordDto: ForgetPasswordDto) {
        httpClient.post("/auth/forgot-password") {setBody(forgetPasswordDto)}

    }

    override suspend fun resetPassword(resetPassDto: ResetPasswordDto) {
        httpClient.post("/auth/reset-password") {setBody(resetPassDto)}
    }

    override suspend fun validateResetPassword(validateDto: ValidateResetPasswordDto) {
        httpClient.post("/auth/reset-password/validate") {setBody(validateDto)}
    }

    override suspend fun resendVerification(userId: Int) {
        httpClient.post("/auth/resend-verification/${userId}")
    }


}
