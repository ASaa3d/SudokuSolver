package com.example.sudokusolver.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForgetPasswordDto(
    val emailOrPhone: String
)
