package com.example.sudokusolver.auth.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.sudokusolver.auth.presentation.state.ResetPasswordCodeState
import org.jetbrains.compose.resources.painterResource
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.wallpaper

@Composable
fun OTPScreenExtendedLayout(
    modifier: Modifier = Modifier,
    state: ResetPasswordCodeState,
    onCodeChange: (String) -> Unit,
    onResendVerificationCode: () -> Unit,
    onFinished: () -> Unit,
    email: String,
    deadlineMs: Long,
    secondsLeft: Int,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = modifier
        ) {
            Image(
                painter = painterResource(Res.drawable.wallpaper),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.wrapContentHeight()
                    .align(Alignment.CenterStart)
                    .widthIn(min = 300.dp, max = 500.dp)
                    .padding(top = 50.dp, bottom = 50.dp, start = 50.dp)
            ) {
                OTPScreenCompactLayout(
                    modifier = Modifier.fillMaxHeight(),
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
    }
}
