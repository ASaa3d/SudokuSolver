package com.example.sudokusolver.auth.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import core.BaseViewModel
import com.example.sudokusolver.auth.domain.usecase.ResetPasswordEmailUseCase
import com.example.sudokusolver.auth.presentation.event.ResetPasswordEmailEvent
import com.example.sudokusolver.auth.presentation.state.ResetPasswordEmailState
import core.InputError
import kotlinx.coroutines.launch

class ResetPasswordEmailViewModel(
    private val forgetPasswordUseCase: ResetPasswordEmailUseCase
) : BaseViewModel<ResetPasswordEmailState, ResetPasswordEmailEvent>(ResetPasswordEmailState()) {

    fun onEmailChange(value: String) =
        updateState { currentState.copy(email = value, emailError = false, error = null) }

    fun onEmailConfirmClick(){
        if (currentState.emailError || currentState.isLoading) return
        viewModelScope.launch {
            updateState { currentState.copy(isLoading = true, error = null) }

            forgetPasswordUseCase(email = currentState.email)
                .onSuccess { sendEvent(ResetPasswordEmailEvent.EmailSubmitted(currentState.email)) }
                .onFailure { e ->
                    updateState {
                        when(e) {
                            is InputError.InvalidEmail -> currentState.copy(emailError = true)
                            else -> currentState.copy(error=e.message)
                        }
                    }
                }
            updateState { currentState.copy(isLoading = false) }
        }
    }
}
