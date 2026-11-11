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
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.window.core.layout.WindowSizeClass
import com.example.sudokusolver.auth.presentation.event.ResetPasswordEmailEvent
import com.example.sudokusolver.auth.presentation.state.ResetPasswordEmailState
import com.example.sudokusolver.auth.presentation.view.ResetPPasswordEmailScreenCompactLayout
import com.example.sudokusolver.auth.presentation.view.ResetPasswordEmailScreenExtendedLayout
import com.example.sudokusolver.auth.presentation.viewmodel.ResetPasswordEmailViewModel
import com.example.sudokusolver.components.CircularSnackBar
import com.example.sudokusolver.navigation.Screen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetPasswordEmailScreen(
    navBackStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordEmailViewModel = koinViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ResetPasswordEmailEvent.EmailSubmitted -> {
                    navBackStack.add(Screen.ResetAccountCode(email = event.email, isVerifyAccount = false))
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
        ResetPasswordEmailScreenContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            onEmailChange = viewModel::onEmailChange,
            onEmailEntered = viewModel::onEmailConfirmClick,
        )
    }
}

@Composable
fun ResetPasswordEmailScreenContent(
    modifier: Modifier = Modifier,
    state: ResetPasswordEmailState,
    onEmailChange: (String) -> Unit,
    onEmailEntered: () -> Unit,
) {
    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val windowSizeClass = adaptiveInfo.windowSizeClass
    val scrollState = rememberScrollState()

    when {
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) ->
            ResetPasswordEmailScreenExtendedLayout(
                modifier = modifier.fillMaxSize().verticalScroll(scrollState),
                state = state,
                onEmailChange = onEmailChange,
                onEmailEntered = onEmailEntered,
            )
        else -> ResetPPasswordEmailScreenCompactLayout(
            modifier = modifier.verticalScroll(scrollState),
            state = state,
            onEmailChange = onEmailChange,
            onEmailEntered = onEmailEntered,
        )
    }
}
