package com.example.sudokusolver.splash.presentation.event

sealed interface SplashEvent {
    data class NavigateToHome(val email: String) : SplashEvent
    data object NavigateToLogin : SplashEvent
}
