package com.example.sudokusolver.auth.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.window.core.layout.WindowSizeClass
import com.example.sudokusolver.auth.presentation.event.ResetPasswordEvent
import com.example.sudokusolver.auth.presentation.state.ResetPasswordState
import com.example.sudokusolver.auth.presentation.view.ResetPasswordCompactLayout
import com.example.sudokusolver.auth.presentation.view.ResetPasswordScreenExtendedLayout
import com.example.sudokusolver.auth.presentation.viewmodel.ResetPasswordViewModel
import com.example.sudokusolver.components.CircularSnackBar
import com.example.sudokusolver.navigation.Screen
import org.koin.compose.viewmodel.koinViewModel
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.password_reset_message

@Composable
fun ResetPasswordScreen(
    navBackStack: NavBackStack<NavKey>,
    email: String,
    code: String,
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordViewModel = koinViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val passwordResetMessage = stringResource(Res.string.password_reset_message)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ResetPasswordEvent.PasswordReset -> {
                    navBackStack.clear()
                    navBackStack.add(
                        Screen.AuthMessage(passwordResetMessage)
                    )
                }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { message ->
            snackBarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.onErrorShown()
        }
    }

    Scaffold(
        topBar = {},
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                snackbar = { data ->
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularSnackBar(message = data.visuals.message)
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        ResetPasswordScreenContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            onPasswordChange = viewModel::onPasswordChange,
            onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
            onPasswordEntered = { viewModel.resetPassword(code = code, email = email) },
        )
    }
}

@Composable
fun ResetPasswordScreenContent(
    modifier: Modifier = Modifier,
    state: ResetPasswordState,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onPasswordEntered: () -> Unit,
) {
    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val windowSizeClass = adaptiveInfo.windowSizeClass
    val scrollState = rememberScrollState()

    when {
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) ->
            ResetPasswordScreenExtendedLayout(
                modifier = modifier.fillMaxSize().verticalScroll(scrollState),
                state = state,
                onPasswordChange = onPasswordChange,
                onConfirmPasswordChange = onConfirmPasswordChange,
                onPasswordEntered = onPasswordEntered,
            )
        else -> ResetPasswordCompactLayout(
            modifier = modifier.verticalScroll(scrollState),
            state = state,
            onPasswordChange = onPasswordChange,
            onConfirmPasswordChange = onConfirmPasswordChange,
            onPasswordEntered = onPasswordEntered,
        )
    }
}
