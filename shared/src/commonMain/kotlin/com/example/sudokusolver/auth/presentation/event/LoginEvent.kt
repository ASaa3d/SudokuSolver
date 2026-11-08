package com.example.sudokusolver.auth.presentation.event

sealed interface LoginEvent {
    data object LoggedIn : LoginEvent
    data object NavigateToSignUp : LoginEvent
    data object NavigateToForgotPassword : LoginEvent
}