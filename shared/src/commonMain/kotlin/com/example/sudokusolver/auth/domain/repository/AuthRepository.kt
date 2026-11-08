package com.example.sudokusolver.auth.domain.repository

import com.example.sudokusolver.auth.domain.model.LoginData
import com.example.sudokusolver.auth.domain.model.SignUpData

interface AuthRepository {

    suspend fun ping():Result<Unit>
    suspend fun login(loginData: LoginData): Result<Unit>
    suspend fun verify(email: String, code: String): Result<Unit>
    suspend fun signUp(signUpData: SignUpData): Result<Int>
    suspend fun resetPasswordEmail(email: String):Result<Unit>
    suspend fun resetPassword(email: String, newPassword: String):Result<Unit>
    suspend fun resetPasswordCode(email: String, code: String):Result<Unit>
    suspend fun resendVerification(userId: Int):Result<Unit>
    suspend fun getToken():Result<String>

}
