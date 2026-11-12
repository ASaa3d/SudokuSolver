package com.example.sudokusolver.auth.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.sudokusolver.auth.presentation.state.ResetPasswordState
import com.example.sudokusolver.components.LoadingShape
import com.example.sudokusolver.components.LogoComp
import com.example.sudokusolver.components.MorphingLoadingIndicator
import org.jetbrains.compose.resources.stringResource
import sudokusolver.shared.generated.resources.Res

import sudokusolver.shared.generated.resources.hide_password

import sudokusolver.shared.generated.resources.password
import sudokusolver.shared.generated.resources.show_password


@Composable
fun ResetPasswordCompactLayout(
    modifier: Modifier = Modifier,
    state: ResetPasswordState,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onPasswordEntered: () -> Unit,
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
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f)
                .padding(vertical = 20.dp, horizontal = 40.dp)
        ) {
            Text(
                text = "Reset Password",
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontFamily = MaterialTheme.typography.labelLarge.fontFamily
            )
            Text(
                modifier = Modifier,
                text = "Enter your new password",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(20.dp))

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
                        "Password should have 1 uppercase letter,1 lowecase letter, 1 symbol(@#$%^&*) and 1 digit and have a min of 8 characters length",
                        color = MaterialTheme.colorScheme.error,
                        maxLines = 3
                    )
                }
            )

            Text(
                text = "Confirm Password",
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
            Spacer(modifier= Modifier.height(30.dp))
            Button(
                modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
                    disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
                onClick = onPasswordEntered,

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
                        text = "Verify Email",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily
                    )
                }
            }
        }

    }
}