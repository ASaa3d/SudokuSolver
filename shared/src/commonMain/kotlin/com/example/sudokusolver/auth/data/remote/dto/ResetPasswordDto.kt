package com.example.sudokusolver.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordDto(
    val email: String,
    val newPassword: String,
    val code: String
)
