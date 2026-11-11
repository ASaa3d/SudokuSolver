package com.example.sudokusolver.auth.presentation.view

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sudokusolver.auth.presentation.state.ResetPasswordCodeState
import com.example.sudokusolver.components.ContainedMorphingLoadingIndicator
import com.example.sudokusolver.components.LoadingShape
import com.example.sudokusolver.components.LogoComp
import com.example.sudokusolver.components.OtpInput
import com.example.sudokusolver.components.ResendCodeTimer

@Composable
fun OTPScreenCompactLayout(
    modifier: Modifier = Modifier,
    state: ResetPasswordCodeState,
    onCodeChange: (String) -> Unit,
    onResendVerificationCode: () -> Unit,
    onFinished: () -> Unit,
    email: String,
    deadlineMs: Long,
    secondsLeft: Int,
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
                text = "OTP",
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontFamily = MaterialTheme.typography.labelLarge.fontFamily
            )
            Text(
                modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically),
                text = "We sent a 6-digit OTP (one-time password) to $email, please enter it below ",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                contentAlignment = Alignment.Center
            )
            {
                if(state.isLoading)
                {
                    ContainedMorphingLoadingIndicator(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        size = 48.dp,
                        shapes = listOf(LoadingShape.Cookie,LoadingShape.Triangle,LoadingShape.Pentagon),
                    )

                }
                else {
                    OtpInput(
                        value = state.code,
                        onValueChange = onCodeChange,
                        onComplete = onFinished,
                        length = 6,
                        isError = (state.error != null)
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
            {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = "email not sent? ",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                ResendCodeTimer(
                    modifier = Modifier.alignByBaseline(),
                    deadlineMs = deadlineMs,
                    secondsLeft = secondsLeft,
                    onResend = onResendVerificationCode
                )
            }

        }

    }
}
