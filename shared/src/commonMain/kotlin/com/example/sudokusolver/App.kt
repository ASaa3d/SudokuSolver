package com.example.sudokusolver

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.sudokusolver.auth.presentation.LoginScreen
import com.example.sudokusolver.navigation.Screen
import com.example.sudokusolver.navigation.navConfig
import com.example.sudokusolver.splash.SplashScreen

import theme.AppTheme


@Composable
@Preview
fun App() {
    val backStack = rememberNavBackStack(navConfig, Screen.Splash)


    AppTheme {
        var showContent by remember { mutableStateOf(false) }
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            // navigating forward: new screen slides in from the right
            transitionSpec = {
                slideInHorizontally(initialOffsetX = { it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { -it / 3 })
            },
            // going back: previous screen slides in from the left
            popTransitionSpec = {
                slideInHorizontally(initialOffsetX = { -it / 3 }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
            },
            // used while the user drags the back gesture (Android predictive back)
            predictivePopTransitionSpec = {
                slideInHorizontally(initialOffsetX = { -it / 3 }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
            },
            entryProvider = entryProvider {
                entry<Screen.Splash> {
                    SplashScreen(
                        navBackStack = backStack
                    )
                }
                entry<Screen.Login> {
                    LoginScreen(
                        navBackStack = backStack,
                    )
                }
                entry<Screen.Home> { email ->
                    LoginScreen(
                        navBackStack = backStack,
                    )
                }
            }
        )

    }
}