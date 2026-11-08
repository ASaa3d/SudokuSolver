package com.example.sudokusolver.splash.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import core.BaseViewModel
import com.example.sudokusolver.splash.domain.usecase.RefreshUseCase
import com.example.sudokusolver.splash.presentation.event.SplashEvent
import com.example.sudokusolver.splash.presentation.state.SplashState
import kotlinx.coroutines.launch

class SplashViewModel(
    private val refreshUseCase: RefreshUseCase
) : BaseViewModel<SplashState, SplashEvent>(SplashState()) {

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            updateState { currentState.copy(isLoading = true, error = null) }
            refreshUseCase()
                .onSuccess { email ->
                    sendEvent(SplashEvent.NavigateToHome(email ?: ""))
                }
                .onFailure {
                    sendEvent(SplashEvent.NavigateToLogin)
                }
            updateState { currentState.copy(isLoading = false) }
        }
    }
}
