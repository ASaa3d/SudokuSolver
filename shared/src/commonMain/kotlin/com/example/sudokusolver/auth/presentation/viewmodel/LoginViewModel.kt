package com.example.sudokusolver.auth.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import core.BaseViewModel
import core.InputError
import com.example.sudokusolver.auth.domain.model.LoginData
import com.example.sudokusolver.auth.domain.usecase.LoginUseCase
import com.example.sudokusolver.auth.presentation.event.LoginEvent
import com.example.sudokusolver.auth.presentation.state.LoginState
import kotlinx.coroutines.launch
import org.koin.core.logger.Logger

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : BaseViewModel<LoginState, LoginEvent>(LoginState()) {

    fun onEmailChange(value: String) =
        updateState { currentState.copy(email = value, emailError = false, error = null) }

    fun onPasswordChange(value: String) =
        updateState { currentState.copy(password = value, passwordError = false, error = null) }
    fun onErrorShown() {
        updateState { currentState.copy(error = null) }
    }
    fun onLoginClick() {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { currentState.copy(isLoading = true, error = null, emailError = false, passwordError = false) }

            loginUseCase(LoginData(state.value.email.trim(), state.value.password))
                .onSuccess { sendEvent(LoginEvent.LoggedIn(currentState.email)) }
                .onFailure { e ->
                    updateState {
                        when (e) {
                            is InputError.InvalidEmail -> currentState.copy(emailError = true)
                            is InputError.InvalidPassword -> currentState.copy(passwordError = true)
                            else -> currentState.copy(error = e.message ?: "Something went wrong")
                        }
                    }
                }
            updateState { currentState.copy(isLoading = false) }
        }
    }

    fun onCreateAccountClick() = sendEvent(LoginEvent.NavigateToSignUp)
    fun onForgotPasswordClick() = sendEvent(LoginEvent.NavigateToForgotPassword)
    fun onGuestLoginClick() = sendEvent(LoginEvent.GuestLogin)
}
