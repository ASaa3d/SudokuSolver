package com.example.sudokusolver.auth.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import core.BaseViewModel
import com.example.sudokusolver.auth.domain.usecase.ResetPasswordUseCase
import com.example.sudokusolver.auth.presentation.event.ResetPasswordEvent
import com.example.sudokusolver.auth.presentation.state.ResetPasswordState
import core.InputError
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : BaseViewModel<ResetPasswordState, ResetPasswordEvent>(ResetPasswordState()) {

    fun onPasswordChange(value: String) =
        updateState { currentState.copy(password = value, passwordError = false, error = null) }

    fun onConfirmPasswordChange(value: String) =
        updateState { currentState.copy(confirmPassword = value, confirmPasswordError = false, error = null) }

    fun onResetPasswordClick(email: String) {
        if (currentState.password != currentState.confirmPassword) {
            updateState { currentState.copy(confirmPasswordError = true, error = "Passwords do not match") }
            return
        }
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { currentState.copy(isLoading = true, error = null) }

            resetPasswordUseCase(email = email, newPassword = currentState.password)
                .onSuccess { sendEvent(ResetPasswordEvent.PasswordReset) }
                .onFailure { e ->
                    updateState {
                        when (e) {
                            is InputError.InvalidPassword -> currentState.copy(passwordError = true)
                            else -> currentState.copy(error = e.message)
                        }
                    }
                }
            updateState { currentState.copy(isLoading = false) }
        }
    }
}
