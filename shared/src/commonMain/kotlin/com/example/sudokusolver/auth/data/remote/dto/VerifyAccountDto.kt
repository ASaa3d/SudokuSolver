package com.example.sudokusolver.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerifyAccountDto(
    val userId: Int,
    val code: String
)
