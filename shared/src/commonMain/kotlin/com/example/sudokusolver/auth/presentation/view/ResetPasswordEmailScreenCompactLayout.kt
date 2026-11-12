package com.example.sudokusolver.auth.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.LazyGridPrefetchStrategy
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
import androidx.compose.material3.LoadingIndicator
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
import com.example.sudokusolver.auth.presentation.state.ResetPasswordEmailState
import com.example.sudokusolver.components.LoadingShape
import com.example.sudokusolver.components.LogoComp
import com.example.sudokusolver.components.MorphingLoadingIndicator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
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
import kotlin.coroutines.Continuation

@Composable
fun ResetPPasswordEmailScreenCompactLayout(
    modifier: Modifier = Modifier,
    state: ResetPasswordEmailState,
    onEmailChange: (String) -> Unit,
    onEmailEntered: () -> Unit,
) {
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
                text = "Forgot Password",
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontFamily = MaterialTheme.typography.labelLarge.fontFamily
            )
            Text(
                modifier = Modifier,
                text = "Enter your email so we can verify it",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(20.dp))
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
                        "Invalid Email",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                maxItemsInEachRow = 2
            )
            {
                Button(
                    modifier = Modifier.weight(1f).widthIn(min = 250.dp)
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
                        disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
                    ),
                    onClick = onEmailEntered,

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
}