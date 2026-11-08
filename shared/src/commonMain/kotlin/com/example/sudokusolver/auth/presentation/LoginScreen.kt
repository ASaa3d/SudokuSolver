package com.example.sudokusolver.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.window.core.layout.WindowSizeClass
import com.example.sudokusolver.auth.presentation.event.LoginEvent
import com.example.sudokusolver.auth.presentation.state.LoginState
import com.example.sudokusolver.auth.presentation.view.LoginScreenCompact
import com.example.sudokusolver.auth.presentation.viewmodel.LoginViewModel
import com.example.sudokusolver.components.LogoComp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.continue_with_google
import sudokusolver.shared.generated.resources.create_now
import sudokusolver.shared.generated.resources.dont_have_an_account
import sudokusolver.shared.generated.resources.email
import sudokusolver.shared.generated.resources.email_placeholder
import sudokusolver.shared.generated.resources.forget_password
import sudokusolver.shared.generated.resources.google_icon_description
import sudokusolver.shared.generated.resources.google_noir
import sudokusolver.shared.generated.resources.hide_password
import sudokusolver.shared.generated.resources.log_in
import sudokusolver.shared.generated.resources.or_text
import sudokusolver.shared.generated.resources.password
import sudokusolver.shared.generated.resources.remember_me
import sudokusolver.shared.generated.resources.show_password
import sudokusolver.shared.generated.resources.sign_in

@Composable
fun LoginScreen(
    navBackStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LoginEvent.LoggedIn -> {}
                LoginEvent.NavigateToSignUp -> {}
                LoginEvent.NavigateToForgotPassword -> {}
            }
        }
    }

    Scaffold(
        topBar = {},

        modifier= modifier.fillMaxSize()
    ) { paddingValues ->
        LoginScreenContent(
            modifier = Modifier.padding(paddingValues),
            state=state,
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
    onPasswordChange: (String)->Unit,
    onEmailChange: (String)->Unit,
    onCreateNewAccount: () -> Unit,
    onForgetPassword: () -> Unit,
    onLogIn: () -> Unit,
) {
    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val windowSizeClass = adaptiveInfo.windowSizeClass


    when {
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) ->
            LoginScreenCompact(
                modifier = modifier,
                state= state,
                onPasswordChange = onPasswordChange,
                onEmailChange = onEmailChange,
                onCreateNewAccount = onCreateNewAccount,
                onForgetPassword = onForgetPassword,
                onLogIn = onLogIn,
            )
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) ->
            LoginScreenCompact(
                modifier = modifier,
                state= state,
                onPasswordChange = onPasswordChange,
                onEmailChange = onEmailChange,
                onCreateNewAccount = onCreateNewAccount,
                onForgetPassword = onForgetPassword,
                onLogIn = onLogIn,
            )
        else -> LoginScreenCompact(
            modifier = modifier,
            state= state,
            onPasswordChange = onPasswordChange,
            onEmailChange = onEmailChange,
            onCreateNewAccount = onCreateNewAccount,
            onForgetPassword = onForgetPassword,
            onLogIn = onLogIn,
        )
    }
}
