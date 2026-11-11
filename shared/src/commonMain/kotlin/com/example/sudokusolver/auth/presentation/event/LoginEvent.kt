package com.example.sudokusolver.auth.presentation.event

sealed interface LoginEvent {
    data class LoggedIn(val email:String) : LoginEvent
    data object GuestLogin : LoginEvent
    data object NavigateToSignUp : LoginEvent
    data object NavigateToForgotPassword : LoginEvent
}