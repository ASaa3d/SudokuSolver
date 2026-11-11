package com.example.sudokusolver.auth.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.sudokusolver.auth.domain.usecase.VerifyAccountUseCase
import com.example.sudokusolver.auth.presentation.event.VerifyAccountEvent
import com.example.sudokusolver.auth.presentation.state.VerifyAccountState
import core.BaseViewModel
import kotlinx.coroutines.launch

class VerifyAccountViewModel(
    private val verifyAccountUseCase: VerifyAccountUseCase
) : BaseViewModel<VerifyAccountState, VerifyAccountEvent>(VerifyAccountState()) {

    fun verify(email: String, code: String) {
        if (currentState.isLoading) return
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            verifyAccountUseCase(email = email, code = code)
                .onSuccess {
                    updateState { copy(isLoading = false) }
                    sendEvent(VerifyAccountEvent.Success)
                }
                .onFailure { e ->
                    updateState { copy(isLoading = false, error = e.message ?: "Verification failed") }
                }
        }
    }
}
