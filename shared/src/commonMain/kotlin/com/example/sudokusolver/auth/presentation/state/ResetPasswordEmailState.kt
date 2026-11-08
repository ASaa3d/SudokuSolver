package com.example.sudokusolver.auth.presentation.state

data class ResetPasswordEmailState(
    val email: String = "",
    val emailError: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false,
)
