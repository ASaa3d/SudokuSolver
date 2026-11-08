package com.example.sudokusolver.auth.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.sudokusolver.auth.presentation.event.SignUpEvent
import com.example.sudokusolver.auth.presentation.viewmodel.SignUpViewModel
import com.example.sudokusolver.navigation.Screen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(
    navBackStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SignUpEvent.SignedUp -> {navBackStack.add(Screen.Home(event.email))}
                is SignUpEvent.NavigateToLogin -> {}
            }
        }
    }

    Scaffold(
        topBar = {},

        modifier= modifier.fillMaxSize()
    ) { paddingValues ->
//        LoginComponent(
//            modifier = Modifier.padding(paddingValues),
//            state=state,
//            onCreateNewAccount = viewModel::onCreateAccountClick,
//            onForgetPassword = viewModel::onForgotPasswordClick,
//            onLogIn = viewModel::onLoginClick,
//            onPasswordChange = viewModel::onPasswordChange,
//            onEmailChange = viewModel::onEmailChange,
//        )

    }
}
