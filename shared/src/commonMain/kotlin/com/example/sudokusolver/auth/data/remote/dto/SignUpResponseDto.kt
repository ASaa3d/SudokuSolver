package com.example.sudokusolver.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponseDto(
    val id: Int,
    val userName: String,
)
