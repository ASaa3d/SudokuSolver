package com.example.sudokusolver.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.sudokusolver.components.LogoComp
import org.jetbrains.compose.resources.painterResource
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.sudoku_logo_dark
import sudokusolver.shared.generated.resources.sudoku_logo_light

@Composable
fun LoginComponent(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val emailState = rememberTextFieldState()
    val passState = rememberTextFieldState()

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
                text = "Sign in",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MaterialTheme.typography.labelLarge.fontFamily
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
            )
            {
                Text(
                    modifier= Modifier.alignByBaseline(),
                    text = "Don't have an account?",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
                Text(
                    modifier = Modifier.clickable(enabled = true, onClick = {}).alignByBaseline(),
                    text = "Create now",
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            }
            Text(
                text="E-mail",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                style = MaterialTheme.typography.titleSmall
            )
            OutlinedTextField(
                state = emailState,
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                placeholder = {Text("example@gmail.com")},
                textStyle = MaterialTheme.typography.bodyMedium,
                shape = RoundedCornerShape(8.dp),

            )

        }

    }

}