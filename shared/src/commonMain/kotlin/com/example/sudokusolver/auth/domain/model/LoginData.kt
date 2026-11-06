package com.example.sudokusolver.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginData (
    val email : String,
    val password: String,
)