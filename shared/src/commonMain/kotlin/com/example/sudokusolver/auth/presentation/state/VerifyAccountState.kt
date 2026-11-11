package com.example.sudokusolver.auth.presentation.state

data class VerifyAccountState(
    val isLoading: Boolean = false,
    val error: String? = null
)
