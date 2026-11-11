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
import com.example.sudokusolver.auth.presentation.state.SignUpState
import org.jetbrains.compose.resources.painterResource
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.wallpaper

@Composable
fun SignUpScreenExtendedLayout(
    modifier: Modifier = Modifier,
    state: SignUpState,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onNavToLogin: () -> Unit,
    onSignUp: () -> Unit,
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
                modifier = Modifier.fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .widthIn(min = 600.dp, max = 800.dp)
                    .padding(top = 50.dp, bottom = 50.dp, start = 50.dp)
            ) {
                SignUpScreenCompactLayout(
                    modifier = Modifier.fillMaxHeight(),
                    state = state,
                    onPasswordChange = onPasswordChange,
                    onConfirmPasswordChange = onConfirmPasswordChange,
                    onEmailChange = onEmailChange,
                    onUsernameChange = onUsernameChange,
                    onNavToLogin = onNavToLogin,
                    onSignUp = onSignUp,
                )
            }
        }
    }
}
