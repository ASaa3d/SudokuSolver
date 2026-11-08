package com.example.sudokusolver.auth.presentation.state

data class SignUpState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val usernameError: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false,
)
