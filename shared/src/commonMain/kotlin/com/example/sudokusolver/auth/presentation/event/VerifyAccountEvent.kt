package com.example.sudokusolver.auth.presentation.event

sealed interface VerifyAccountEvent {
    data object Success : VerifyAccountEvent
}
