package com.example.sudokusolver.auth.domain.model

data class SignUpData(
    val userName: String,
    val email: String,
    val phone: String,
    val password: String,
)
