package com.example.sudokusolver.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResendPasswordChangeDto(
    val email: String
)
