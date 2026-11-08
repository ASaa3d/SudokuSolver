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
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import com.example.sudokusolver.auth.presentation.event.LoginEvent
import com.example.sudokusolver.auth.presentation.state.LoginState
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
        LoginComponent(
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
fun LoginComponent(
    modifier: Modifier = Modifier,
    state: LoginState,
    onPasswordChange: (String)->Unit,
    onEmailChange: (String)->Unit,
    onCreateNewAccount: () -> Unit,
    onForgetPassword: () -> Unit,
    onLogIn: () -> Unit,
) {
    val scrollState = rememberScrollState()
    var isPasswordVisible by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 15.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),

        )
    {
        LogoComp(modifier.fillMaxHeight(0.2f))

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxHeight(0.8f).padding(start = 40.dp, end = 40.dp, top = 40.dp)
        ) {
            Text(
                text = stringResource(Res.string.sign_in),
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontFamily = MaterialTheme.typography.labelLarge.fontFamily
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
            )
            {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = stringResource(Res.string.dont_have_an_account),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
                Text(
                    modifier = Modifier.clickable(enabled = true, onClick = onCreateNewAccount)
                        .alignByBaseline(),
                    text = stringResource(Res.string.create_now),
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(Res.string.email),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                style = MaterialTheme.typography.titleSmall
            )
            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                placeholder = { Text(stringResource(Res.string.email_placeholder)) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(Res.string.password),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                style = MaterialTheme.typography.titleSmall
            )
            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (isPasswordVisible) stringResource(Res.string.hide_password) else stringResource(Res.string.show_password)
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                shape = RoundedCornerShape(8.dp),
            )
            Row(
                modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Row(
                    modifier = Modifier.alignByBaseline(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                )
                {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { checked = it },
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        modifier = Modifier.alignByBaseline(),
                        text = stringResource(Res.string.remember_me),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )

                }



                Text(
                    modifier = Modifier.clickable(enabled = true, onClick = onForgetPassword)
                        .alignByBaseline(),
                    text = stringResource(Res.string.forget_password),
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 30.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
                    disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
                onClick = onLogIn,

                )
            {
                Text(
                    text = stringResource(Res.string.log_in),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily
                )
            }


            Row(
                modifier = Modifier.fillMaxWidth().padding(top=20.dp),
                verticalAlignment = Alignment.CenterVertically

            )
            {
                HorizontalDivider(color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.fillMaxWidth(0.45f))
                Text(
                    modifier = Modifier.alignByBaseline().padding(horizontal = 5.dp),
                    text = stringResource(Res.string.or_text),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 12.sp
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.tertiary)
            }

            Button(
                modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 30.dp),
                shape = CircleShape,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
                    disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
                onClick = { //TODO
                },

                )
            {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                )
                {
                    Icon(
                        modifier = Modifier.size(25.dp).align(Alignment.CenterStart),
                        painter = painterResource(Res.drawable.google_noir),
                        contentDescription = stringResource(Res.string.google_icon_description)
                    )
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(Res.string.continue_with_google),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500,
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily
                    )
                }
            }








        }

    }

}