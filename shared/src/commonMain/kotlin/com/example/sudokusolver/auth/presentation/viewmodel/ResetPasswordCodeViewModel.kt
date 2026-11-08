package com.example.sudokusolver.auth.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import core.BaseViewModel
import com.example.sudokusolver.auth.domain.usecase.ResetPasswordCodeUseCase
import com.example.sudokusolver.auth.domain.usecase.VerifyAccountUseCase
import com.example.sudokusolver.auth.presentation.event.ResetPasswordCodeEvent
import com.example.sudokusolver.auth.presentation.state.ResetPasswordCodeState
import core.InputError
import kotlinx.coroutines.launch

class ResetPasswordCodeViewModel(
    private val verifyAccountUseCase: VerifyAccountUseCase,
    private val resetPasswordCodeUseCase: ResetPasswordCodeUseCase
) : BaseViewModel<ResetPasswordCodeState, ResetPasswordCodeEvent>(ResetPasswordCodeState()) {

    fun onCodeChange(value: String) =
        updateState { currentState.copy(code = value, codeError = false, error = null) }

    fun verifyAccount(email: String) {
        if (currentState.code.isBlank() || currentState.isLoading) return
        viewModelScope.launch {
            updateState { currentState.copy(isLoading = true, error = null) }

            verifyAccountUseCase(email = email, code = currentState.code)
                .onSuccess { sendEvent(ResetPasswordCodeEvent.AccountVerified(email, currentState.code)) }
                .onFailure { e ->
                    updateState {
                        when (e) {
                            is InputError.InvalidCode -> currentState.copy(codeError = true)
                            else -> currentState.copy(error = e.message)
                        }
                    }
                }
            updateState { currentState.copy(isLoading = false) }
        }
    }

    fun resetPasswordCode(email: String) {
        if (currentState.code.isBlank() || currentState.isLoading) return
        viewModelScope.launch {
            updateState { currentState.copy(isLoading = true, error = null) }

            resetPasswordCodeUseCase(email = email, code = currentState.code)
                .onSuccess { sendEvent(ResetPasswordCodeEvent.ResetPasswordCodeValidated(email, currentState.code)) }
                .onFailure { e ->
                    updateState {
                        when (e) {
                            is InputError.InvalidCode -> currentState.copy(codeError = true)
                            else -> currentState.copy(error = e.message)
                        }
                    }
                }
            updateState { currentState.copy(isLoading = false) }
        }
    }
}
