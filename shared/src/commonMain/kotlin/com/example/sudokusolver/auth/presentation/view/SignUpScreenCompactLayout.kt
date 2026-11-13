package com.example.sudokusolver.auth.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.sudokusolver.auth.presentation.state.LoginState
import com.example.sudokusolver.auth.presentation.state.SignUpState
import com.example.sudokusolver.components.LoadingShape
import com.example.sudokusolver.components.LogoComp
import com.example.sudokusolver.components.MorphingLoadingIndicator
import org.jetbrains.compose.resources.stringResource
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.confirm_password
import sudokusolver.shared.generated.resources.email
import sudokusolver.shared.generated.resources.email_placeholder
import sudokusolver.shared.generated.resources.hide_password
import sudokusolver.shared.generated.resources.invalid_email
import sudokusolver.shared.generated.resources.log_in
import sudokusolver.shared.generated.resources.name
import sudokusolver.shared.generated.resources.name_placeholder
import sudokusolver.shared.generated.resources.old_friend
import sudokusolver.shared.generated.resources.password
import sudokusolver.shared.generated.resources.password_requirement
import sudokusolver.shared.generated.resources.show_password
import sudokusolver.shared.generated.resources.sign_up
import sudokusolver.shared.generated.resources.username_requirement

@Composable
fun SignUpScreenCompactLayout(
    modifier: Modifier = Modifier,
    state: SignUpState,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onNavToLogin: () -> Unit,
    onSignUp: () -> Unit,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxSize()

            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 30.dp, horizontal = 25.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    )
    {
        LogoComp(Modifier.fillMaxWidth().fillMaxHeight(0.2f))

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f).padding(vertical = 20.dp, horizontal = 40.dp)
        ) {
            Text(
                text = stringResource(Res.string.sign_up),
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
                    text = stringResource(Res.string.old_friend),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
                Text(
                    modifier = Modifier.clickable(enabled = true, onClick = onNavToLogin)
                        .alignByBaseline(),
                    text = stringResource(Res.string.log_in),
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.height(20.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                maxItemsInEachRow = 2
            )
            {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(1f).widthIn(min = 250.dp)
                        .wrapContentHeight()
                )
                {
                    Text(
                        text = stringResource(Res.string.name),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.titleSmall
                    )
                    OutlinedTextField(
                        enabled = !state.isLoading,
                        value = state.username,
                        onValueChange = onUsernameChange,
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        placeholder = { Text(stringResource(Res.string.name_placeholder)) },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        isError = state.usernameError,
                        supportingText = {
                            if (state.usernameError) Text(
                                stringResource(Res.string.username_requirement),
                                color = MaterialTheme.colorScheme.error,
                                maxLines = 3
                            )
                        }
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(1f).widthIn(min = 250.dp)
                        .wrapContentHeight()
                )
                {
                    Text(
                        text = stringResource(Res.string.email),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.titleSmall
                    )
                    OutlinedTextField(
                        enabled = !state.isLoading,
                        value = state.email,
                        onValueChange = onEmailChange,
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        placeholder = { Text(stringResource(Res.string.email_placeholder)) },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = state.emailError,
                        supportingText = {
                            if (state.emailError) Text(
                                stringResource(Res.string.invalid_email),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(1f).widthIn(min = 250.dp)
                        .wrapContentHeight()
                )
                {
                    Text(
                        text = stringResource(Res.string.password),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.titleSmall
                    )
                    OutlinedTextField(
                        enabled = !state.isLoading,
                        value = state.password,
                        onValueChange = onPasswordChange,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (isPasswordVisible) stringResource(Res.string.hide_password) else stringResource(
                                        Res.string.show_password
                                    )
                                )
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        shape = RoundedCornerShape(8.dp),
                        isError = state.passwordError,
                        supportingText = {
                            if (state.passwordError) Text(
                                stringResource(Res.string.password_requirement),
                                color = MaterialTheme.colorScheme.error,
                                maxLines = 3
                            )
                        }
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(1f).widthIn(min = 250.dp)
                        .wrapContentHeight()
                )
                {
                    Text(
                        text = stringResource(Res.string.confirm_password),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.titleSmall
                    )
                    OutlinedTextField(
                        enabled = !state.isLoading,
                        value = state.confirmPassword,
                        onValueChange = onConfirmPasswordChange,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        shape = RoundedCornerShape(8.dp),
                        isError = state.passwordError,
                    )
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth().padding(top = 30.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
                    disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
                onClick = onSignUp,

                )
            {
                if (state.isLoading) {
                    MorphingLoadingIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        size = 32.dp,
                        morphDurationMillis = 500,
                    )

                } else {
                    Text(
                        text = stringResource(Res.string.sign_up),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily
                    )
                }
            }
        }
    }

}