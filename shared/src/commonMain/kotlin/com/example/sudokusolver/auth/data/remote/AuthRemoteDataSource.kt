package com.example.sudokusolver.auth.data.remote

import com.example.sudokusolver.auth.data.remote.dto.ForgetPasswordDto
import com.example.sudokusolver.auth.data.remote.dto.ResendPasswordChangeDto
import com.example.sudokusolver.auth.data.remote.dto.ResendVerificationDto
import com.example.sudokusolver.auth.data.remote.dto.ResetPasswordDto
import com.example.sudokusolver.auth.data.remote.dto.SignUpDto
import com.example.sudokusolver.auth.data.remote.dto.ValidateResetPasswordDto
import com.example.sudokusolver.auth.data.remote.dto.VerifyAccountDto
import com.example.sudokusolver.auth.domain.model.LoginData
import com.example.sudokusolver.auth.domain.model.SignUpData

interface AuthRemoteDataSource {
    suspend fun login(loginData: LoginData): String
    suspend fun verify(verifyDto: VerifyAccountDto): Unit
    suspend fun signUp(signUpData: SignUpData): Int
    suspend fun forgotPassword(forgetPasswordDto: ForgetPasswordDto):Unit
    suspend fun resetPassword(resetPassDto: ResetPasswordDto):Unit
    suspend fun validateResetPassword(validateDto: ValidateResetPasswordDto):Unit
    suspend fun resendVerification(resendDto: ResendVerificationDto):Unit
    suspend fun resendPasswordChange(dto: ResendPasswordChangeDto):Unit
}
