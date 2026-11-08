package com.example.sudokusolver.auth.presentation.event

sealed interface ResetPasswordCodeEvent {
    data class AccountVerified(val email: String, val code: String) : ResetPasswordCodeEvent
    data class ResetPasswordCodeValidated(val email: String, val code: String) : ResetPasswordCodeEvent
}
