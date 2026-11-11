package com.example.sudokusolver.splash.presentation.event

sealed interface SplashEvent {
    data class NavigateToHome(val email: String, val isGuest: Boolean = false) : SplashEvent
    data object NavigateToLogin : SplashEvent
    data object NavigateToSignUp : SplashEvent
    data class NavigateToVerifyAccount(val email: String, val code: String) : SplashEvent
    data object NavigateToResetAccountEmail : SplashEvent
    data class NavigateToResetAccountCode(val email: String, val isVerifyAccount: Boolean) : SplashEvent
    data class NavigateToResetPassword(val email: String, val code: String) : SplashEvent
    data class ResolveTarget(val email: String?) : SplashEvent
}
