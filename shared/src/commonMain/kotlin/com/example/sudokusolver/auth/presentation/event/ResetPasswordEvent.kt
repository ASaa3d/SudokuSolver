package com.example.sudokusolver.auth.presentation.event

sealed interface ResetPasswordEvent {
    data object PasswordReset : ResetPasswordEvent
}
