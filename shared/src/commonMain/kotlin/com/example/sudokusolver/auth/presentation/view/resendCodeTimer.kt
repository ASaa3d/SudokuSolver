package com.example.sudokusolver.auth.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import org.jetbrains.compose.resources.stringResource
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.resend_code
import sudokusolver.shared.generated.resources.resend_code_in
import kotlin.time.Clock

@Composable
fun ResendCodeTimer(
    deadlineMs: Long,
    secondsLeft: Int,
    onResend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val enabled = secondsLeft == 0

    Text(
        text = if (enabled) {
            stringResource(Res.string.resend_code)
        } else {
            val mm = (secondsLeft / 60).toString().padStart(2, '0')
            val ss = (secondsLeft % 60).toString().padStart(2, '0')
            stringResource(Res.string.resend_code_in, "$mm:$ss")
        },
        modifier = modifier.clickable(
            enabled = enabled,
            onClick = onResend,
        ),
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = if (enabled) FontWeight.SemiBold else FontWeight.Normal,
        style = TextStyle(
            textDecoration = if (enabled) TextDecoration.Underline else TextDecoration.None,
        ),
    )
}

fun secondsUntil(deadlineMs: Long): Int {
    val remainingMs = deadlineMs - nowMs()
    return if (remainingMs <= 0) 0 else ((remainingMs + 999) / 1000).toInt()
}

fun nowMs(): Long = Clock.System.now().toEpochMilliseconds()
