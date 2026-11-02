package com.example.sudokusolver

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.sudokusolver.auth.presentation.LoginScreen
import com.example.sudokusolver.navigation.Home
import com.example.sudokusolver.navigation.Splash
import com.example.sudokusolver.navigation.navConfig
import com.example.sudokusolver.splash.SplashScreen
import com.example.sudokusolver.sudoku.SudokuViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.compose_multiplatform
import theme.AppTheme


@Composable
@Preview
fun App(
    sudokuViewModel: SudokuViewModel = koinViewModel()
) {
    val backStack = rememberNavBackStack(navConfig, if (showSplash) Splash else Home )


    AppTheme {
        var showContent by remember { mutableStateOf(false) }
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Splash> {
                    SplashScreen(
                        navBackStack = backStack
                    )
                }
                entry<Home> {
                    LoginScreen(
                        navBackStack = backStack,
                    )
                }
            }
        )

    }
}