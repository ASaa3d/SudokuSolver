package com.example.sudokusolver.auth.presentation.event

sealed interface ResetPasswordEmailEvent {
    data class EmailSubmitted(val email:String) : ResetPasswordEmailEvent
}