package com.example.sudokusolver.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerifyAccountDto(
    val email: String,
    val code: String
)
