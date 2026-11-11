package com.example.sudokusolver.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.window.core.layout.WindowSizeClass
import com.example.sudokusolver.auth.presentation.event.LoginEvent
import com.example.sudokusolver.auth.presentation.state.LoginState
import com.example.sudokusolver.auth.presentation.view.LoginScreenCompactLayout
import com.example.sudokusolver.auth.presentation.view.LoginScreenExtendedLayout
import com.example.sudokusolver.auth.presentation.viewmodel.LoginViewModel
import com.example.sudokusolver.components.CircularSnackBar
import com.example.sudokusolver.navigation.Screen
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun LoginScreen(
    navBackStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.LoggedIn -> {
                    navBackStack.add(Screen.Home(event.email,false))
                    navBackStack.removeLastOrNull()
                }
                is LoginEvent.NavigateToSignUp -> {
                    navBackStack.add(Screen.SignUp)
                }
                is LoginEvent.NavigateToForgotPassword -> {
                    navBackStack.add(Screen.ResetAccountEmail)
                }
                is LoginEvent.GuestLogin -> {
                    navBackStack.add(Screen.Home("guest@user.com",true))
                }
            }
        }
    }
    LaunchedEffect(state.error) {
        state.error?.let { message ->
            val result = snackBarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.onErrorShown() // clear the error so it shows only once
        }
    }

    Scaffold(
        topBar = {},
        snackbarHost = {SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            snackbar = { data ->
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularSnackBar(message = data.visuals.message)
                }
            }
        )},

        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LoginScreenContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            onCreateNewAccount = viewModel::onCreateAccountClick,
            onForgetPassword = viewModel::onForgotPasswordClick,
            onLogIn = viewModel::onLoginClick,
            onPasswordChange = viewModel::onPasswordChange,
            onEmailChange = viewModel::onEmailChange,
        )

    }
}


@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    state: LoginState,
    onPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onCreateNewAccount: () -> Unit,
    onForgetPassword: () -> Unit,
    onLogIn: () -> Unit,
) {
    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val windowSizeClass = adaptiveInfo.windowSizeClass
    val scrollState = rememberScrollState()


    when {
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) ->
            LoginScreenExtendedLayout(
                modifier = modifier.fillMaxSize().verticalScroll(scrollState),
                state = state,
                onPasswordChange = onPasswordChange,
                onEmailChange = onEmailChange,
                onCreateNewAccount = onCreateNewAccount,
                onForgetPassword = onForgetPassword,
                onLogIn = onLogIn,
            )

        else -> LoginScreenCompactLayout(
            modifier = modifier.verticalScroll(scrollState),
            state = state,
            onPasswordChange = onPasswordChange,
            onEmailChange = onEmailChange,
            onCreateNewAccount = onCreateNewAccount,
            onForgetPassword = onForgetPassword,
            onLogIn = onLogIn,
        )
    }
}
