package com.example.sudokusolver.auth.presentation.event

sealed interface SignUpEvent {
    data class SignedUp(val email:String) : SignUpEvent
    data object NavigateToLogin : SignUpEvent
}