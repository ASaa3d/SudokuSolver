package com.example.sudokusolver.auth.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import core.BaseViewModel
import com.example.sudokusolver.auth.domain.model.SignUpData
import core.InputError
import com.example.sudokusolver.auth.domain.usecase.SignUpUseCase
import com.example.sudokusolver.auth.presentation.event.SignUpEvent
import com.example.sudokusolver.auth.presentation.state.SignUpState
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
) : BaseViewModel<SignUpState, SignUpEvent>(SignUpState()) {

    fun onEmailChange(value: String) =
        updateState { currentState.copy(email = value, emailError = false, error = null) }

    fun onPasswordChange(value: String) =
        updateState { currentState.copy(password = value, passwordError = false, error = null) }

    fun onConfirmPasswordChange(value: String) =
        updateState { currentState.copy(confirmPassword = value) }

    fun onUserNameChange(value: String) =
        updateState { currentState.copy(username = value, usernameError = false, error = null) }

    fun onLoginClick(){ sendEvent(SignUpEvent.NavigateToLogin) }

    fun onSignUpClick()
    {
        if (currentState.emailError || currentState.passwordError|| currentState.usernameError || currentState.isLoading) return
        viewModelScope.launch {
            updateState { currentState.copy(isLoading = true, error = null) }

            signUpUseCase(SignUpData(email=state.value.email.trim(), password = state.value.password, userName = state.value.username ))
                .onSuccess { sendEvent(SignUpEvent.SignedUp(currentState.email.trim())) }
                .onFailure { e ->
                    updateState {
                        when (e) {
                            is InputError.InvalidEmail -> currentState.copy(emailError=true)
                            is InputError.InvalidPassword -> currentState.copy(passwordError=true)
                            is InputError.InvalidUserName -> currentState.copy(usernameError=true)
                            else -> currentState.copy(error=e.message)
                        }
                    }
                }
            updateState { currentState.copy(isLoading = false) }
        }
    }
}
