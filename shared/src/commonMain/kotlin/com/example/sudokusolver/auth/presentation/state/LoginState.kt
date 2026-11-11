package com.example.sudokusolver.auth.presentation.state

data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false,
)
