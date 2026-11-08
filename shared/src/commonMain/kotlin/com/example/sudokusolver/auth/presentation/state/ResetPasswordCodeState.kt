package com.example.sudokusolver.auth.presentation.state

data class ResetPasswordCodeState(
    val code: String = "",
    val codeError: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false,
)
