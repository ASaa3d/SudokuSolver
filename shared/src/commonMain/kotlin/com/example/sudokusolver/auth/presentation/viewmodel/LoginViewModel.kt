package com.example.sudokusolver.auth.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import core.BaseViewModel
import com.example.sudokusolver.auth.domain.model.LoginData
import com.example.sudokusolver.auth.domain.usecase.LoginUseCase
import com.example.sudokusolver.auth.presentation.event.LoginEvent
import com.example.sudokusolver.auth.presentation.state.LoginState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : BaseViewModel<LoginState, LoginEvent>(LoginState()) {

    fun onEmailChange(value: String) =
        updateState { currentState.copy(email = value, error = null) }

    fun onPasswordChange(value: String) =
        updateState { currentState.copy(password = value, error = null) }

    fun onLoginClick() {
        if (currentState.email.isBlank() || currentState.password.isBlank() || currentState.isLoading) return

        viewModelScope.launch {
            updateState { currentState.copy(isLoading = true, error = null) }

            loginUseCase(LoginData(state.value.email.trim(), state.value.password))
                .onSuccess { sendEvent(LoginEvent.LoggedIn) }
                .onFailure { e ->
                    updateState {
                        currentState.copy(error = e.message ?: "Something went wrong")
                    }
                }
            updateState { currentState.copy(isLoading = false) }
        }
    }

    fun onCreateAccountClick() = sendEvent(LoginEvent.NavigateToSignUp)
    fun onForgotPasswordClick() = sendEvent(LoginEvent.NavigateToForgotPassword)
}