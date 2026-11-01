package com.example.sudokusolver.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.app.splash.SudokuSplash

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    SudokuSplash(modifier=modifier, onFinished = onFinished)
}