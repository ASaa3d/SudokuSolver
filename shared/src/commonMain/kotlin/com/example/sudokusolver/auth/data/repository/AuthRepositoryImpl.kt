package com.example.sudokusolver.auth.data.repository

import com.example.sudokusolver.auth.data.remote.AuthRemoteDataSource
import com.example.sudokusolver.auth.data.remote.dto.ForgetPasswordDto
import com.example.sudokusolver.auth.data.remote.dto.ResetPasswordDto
import com.example.sudokusolver.auth.data.remote.dto.ValidateResetPasswordDto
import com.example.sudokusolver.auth.data.remote.dto.VerifyAccountDto
import com.example.sudokusolver.auth.domain.model.LoginData
import com.example.sudokusolver.auth.domain.model.SignUpData
import com.example.sudokusolver.auth.domain.repository.AuthRepository
import com.example.sudokusolver.datastore.LocalDataSource
import core.safeCall

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: LocalDataSource,

) : AuthRepository{
    companion object
    {
        private const val TOKEN_KEY :String = "access_token"
        private const val USER_ID_KEY :String = "user_id"
    }
    override suspend fun ping() : Result<Unit> = safeCall { remoteDataSource.ping() }


    override suspend fun login(loginData: LoginData): Result<Unit> = safeCall {
        remoteDataSource.login(loginData).also { token ->
            localDataSource.save(TOKEN_KEY, token)
        }
    }

    override suspend fun verify(
        email: String,
        code: String
    ): Result<Unit> = safeCall { remoteDataSource.verify(VerifyAccountDto(email = email, code = code)) }

    override suspend fun signUp(signUpData: SignUpData): Result<Int> =safeCall{
        remoteDataSource.signUp(signUpData).also { userId ->
            localDataSource.save(USER_ID_KEY,userId.toString())
        }
    }

    override suspend fun resetPasswordEmail(email: String): Result<Unit> = safeCall { remoteDataSource.forgotPassword(
        ForgetPasswordDto(email)) }

    override suspend fun resetPassword(
        email: String,
        newPassword: String
    ): Result<Unit> = safeCall { remoteDataSource.resetPassword(ResetPasswordDto(email = email, newPassword = newPassword)) }

    override suspend fun resetPasswordCode(
        email: String,
        code: String
    ): Result<Unit> = safeCall { remoteDataSource.validateResetPassword(ValidateResetPasswordDto(email = email, code = code)) }

    override suspend fun resendVerification(userId: Int): Result<Unit> = safeCall { remoteDataSource.resendVerification(userId) }

    override suspend fun getToken(): Result<String> = safeCall { localDataSource.get(TOKEN_KEY) }
}
