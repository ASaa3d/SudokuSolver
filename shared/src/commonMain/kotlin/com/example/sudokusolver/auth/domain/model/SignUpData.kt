package com.example.sudokusolver.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpData(
    val userName: String,
    val email: String,
    val password: String,
)
