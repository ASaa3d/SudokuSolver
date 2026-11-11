package com.example.sudokusolver

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.deeplink.DeepLinkRequest
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.sudokusolver.auth.presentation.LoginScreen
import com.example.sudokusolver.auth.presentation.OTPScreen
import com.example.sudokusolver.auth.presentation.ResetPasswordEmailScreen
import com.example.sudokusolver.auth.presentation.ResetPasswordScreen
import com.example.sudokusolver.auth.presentation.SignUpScreen
import com.example.sudokusolver.auth.presentation.VerifyAccountScreen
import com.example.sudokusolver.di.PlatformBackSync
import com.example.sudokusolver.navigation.Screen
import com.example.sudokusolver.navigation.navConfig
import com.example.sudokusolver.splash.SplashScreen

import theme.AppTheme


@Composable
@Preview
fun App(
    deepLinkRequest: DeepLinkRequest?=null,
    initialTarget: String? = null,
) {
    val startTarget = deepLinkRequest?.uri?.toString() ?: initialTarget
    val backStack = rememberNavBackStack(
        navConfig,
        Screen.Splash(target = startTarget)
    )
    // Links that arrive while the app is already running (Android onNewIntent)
    var handledUri by rememberSaveable { mutableStateOf(deepLinkRequest?.uri) }

    LaunchedEffect(deepLinkRequest?.uri) {
        val uri = deepLinkRequest?.uri
        if (uri != null && uri != handledUri) {
            handledUri = uri
            backStack.clear()
            backStack.add(Screen.Splash(target = uri.toString()))
        }
    }
    PlatformBackSync(backStack)
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
                entry<Screen.Splash> { (target) ->
                    SplashScreen(
                        navBackStack = backStack,
                        target = target
                    )
                }
                entry<Screen.Login> {
                    LoginScreen(
                        navBackStack = backStack,
                    )
                }
                entry<Screen.SignUp> {
                    SignUpScreen(
                        navBackStack = backStack,
                    )
                }
                entry<Screen.VerifyAccount> { (email, code) ->
                    VerifyAccountScreen(
                        navBackStack = backStack,
                        email = email,
                        code = code
                    )
                }
                entry<Screen.ResetAccountEmail> {
                    ResetPasswordEmailScreen(
                        navBackStack = backStack,
                    )
                }
                entry<Screen.ResetPassword> {(email,code)->
                    ResetPasswordScreen(
                        navBackStack = backStack,
                        email = email,
                        code = code
                    )
                }
                entry<Screen.ResetAccountCode> { (email,isVerifyAcc) ->
                    OTPScreen(
                        navBackStack = backStack,
                        email=email,
                        isVerifyAccount = isVerifyAcc
                    )
                }
                entry<Screen.Home> {(email,isGuest)->
                }
            }
        )

    }
}