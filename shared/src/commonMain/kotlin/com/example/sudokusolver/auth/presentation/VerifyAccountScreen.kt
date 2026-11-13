package com.example.sudokusolver.auth.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.sudokusolver.auth.presentation.event.VerifyAccountEvent
import com.example.sudokusolver.auth.presentation.viewmodel.VerifyAccountViewModel
import com.example.sudokusolver.navigation.Screen
import org.koin.compose.viewmodel.koinViewModel
import theme.SlateBlue
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.account_verified_message
import sudokusolver.shared.generated.resources.back_to_login
import sudokusolver.shared.generated.resources.short_moment
import sudokusolver.shared.generated.resources.try_again
import sudokusolver.shared.generated.resources.verification_failed
import sudokusolver.shared.generated.resources.verifying_account

@Composable
fun VerifyAccountScreen(
    navBackStack: NavBackStack<NavKey>,
    email: String,
    code: String,
    modifier: Modifier = Modifier,
    viewModel: VerifyAccountViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val accountVerifiedMessage = stringResource(Res.string.account_verified_message)

    LaunchedEffect(email, code) {
        viewModel.verify(email = email, code = code)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is VerifyAccountEvent.Success -> {
                    navBackStack.clear()
                    navBackStack.add(
                        Screen.AuthMessage(accountVerifiedMessage)
                    )
                }
            }
        }
    }

    VerifyAccountContent(
        errorMessage = state.error,
        onRetry = { viewModel.verify(email = email, code = code) },
        onBackToLogin = {
            navBackStack.clear()
            navBackStack.add(Screen.Login)
        },
        modifier = Modifier.background(if (isSystemInDarkTheme()) SlateBlue  else MaterialTheme.colorScheme.background ),
    )
}

@Composable
private fun VerifyAccountContent(
    errorMessage: String?,
    onRetry: () -> Unit,
    onBackToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = errorMessage != null,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "verify-account-state",
        ) { hasError ->
            Column(
                modifier = Modifier.widthIn(max = 360.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (hasError) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier.size(72.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.ErrorOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.size(36.dp),
                            )
                        }
                    }
                    Text(
                        text = stringResource(Res.string.verification_failed),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = errorMessage.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                    Button(
                        onClick = onRetry,
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text(stringResource(Res.string.try_again)) }
                    OutlinedButton(
                        onClick = onBackToLogin,
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text(stringResource(Res.string.back_to_login)) }
                } else {
                    CircularProgressIndicator(modifier = Modifier.size(56.dp))
                    Text(
                        text = stringResource(Res.string.verifying_account),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = stringResource(Res.string.short_moment),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}