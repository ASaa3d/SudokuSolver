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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.window.core.layout.WindowSizeClass
import com.example.sudokusolver.auth.presentation.event.ResetPasswordCodeEvent
import com.example.sudokusolver.auth.presentation.state.ResetPasswordCodeState
import com.example.sudokusolver.auth.presentation.view.OTPScreenCompactLayout
import com.example.sudokusolver.auth.presentation.view.OTPScreenExtendedLayout
import com.example.sudokusolver.auth.presentation.viewmodel.ResetPasswordCodeViewModel
import com.example.sudokusolver.components.CircularSnackBar
import com.example.sudokusolver.auth.presentation.view.nowMs
import com.example.sudokusolver.auth.presentation.view.secondsUntil
import com.example.sudokusolver.navigation.Screen
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import org.koin.compose.viewmodel.koinViewModel
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.account_verified_message

@Composable
fun OTPScreen(
    navBackStack: NavBackStack<NavKey>,
    email: String,
    isVerifyAccount: Boolean,
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordCodeViewModel = koinViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val accountVerifiedMessage = stringResource(Res.string.account_verified_message)

    val totalSeconds = 120
    var deadlineMs by rememberSaveable {
        mutableLongStateOf(nowMs() + totalSeconds * 1000L)
    }
    var secondsLeft by remember(deadlineMs) {
        mutableIntStateOf(secondsUntil(deadlineMs))
    }

    LaunchedEffect(deadlineMs) {
        while (true) {
            secondsLeft = secondsUntil(deadlineMs)
            if (secondsLeft == 0) break
            delay(250.milliseconds)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ResetPasswordCodeEvent.AccountVerified -> {
                    navBackStack.clear()
                    navBackStack.add(
                        Screen.AuthMessage(accountVerifiedMessage)
                    )
                }
                is ResetPasswordCodeEvent.ResetPasswordCodeValidated -> {
                    navBackStack.removeLast()
                    navBackStack.removeLast()
                    navBackStack.add(Screen.ResetPassword(email = event.email,code=state.code))
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
        OTPScreenContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            email = email,
            deadlineMs = deadlineMs,
            secondsLeft = secondsLeft,
            onCodeChange = viewModel::onCodeChange,
            onResendVerificationCode = {
                if (isVerifyAccount) viewModel.resendVerification(email) else viewModel.resendPasswordChange(email)
                deadlineMs = nowMs() + totalSeconds * 1000L
            },
            onFinished = { if (isVerifyAccount) viewModel.verifyAccount(email) else viewModel.resetPasswordCode(email) },
        )
    }
}

@Composable
fun OTPScreenContent(
    modifier: Modifier = Modifier,
    state: ResetPasswordCodeState,
    email: String,
    deadlineMs: Long,
    secondsLeft: Int,
    onCodeChange: (String) -> Unit,
    onResendVerificationCode: () -> Unit,
    onFinished: () -> Unit,
) {
    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val windowSizeClass = adaptiveInfo.windowSizeClass
    val scrollState = rememberScrollState()

    when {
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) ->
            OTPScreenExtendedLayout(
                modifier = modifier.fillMaxSize().verticalScroll(scrollState),
                state = state,
                onCodeChange = onCodeChange,
                onResendVerificationCode = onResendVerificationCode,
                onFinished = onFinished,
                email = email,
                deadlineMs = deadlineMs,
                secondsLeft = secondsLeft,
            )
        else -> OTPScreenCompactLayout(
            modifier = modifier.verticalScroll(scrollState),
            state = state,
            onCodeChange = onCodeChange,
            onResendVerificationCode = onResendVerificationCode,
            onFinished = onFinished,
            email = email,
            deadlineMs = deadlineMs,
            secondsLeft = secondsLeft,
        )
    }
}
