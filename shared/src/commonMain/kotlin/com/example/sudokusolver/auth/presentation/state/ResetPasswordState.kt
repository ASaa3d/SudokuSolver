package com.example.sudokusolver.auth.presentation.state

data class ResetPasswordState(
    val password: String = "",
    val confirmPassword: String = "",
    val passwordError: Boolean = false,
    val confirmPasswordError: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false,
)
