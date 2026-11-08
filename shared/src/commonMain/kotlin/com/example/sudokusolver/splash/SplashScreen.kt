package com.example.sudokusolver.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.sudokusolver.navigation.Screen

@Composable
fun SplashScreen(
    navBackStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier
) {
    SudokuSplash(modifier=modifier, onFinished = {
        navBackStack.add(Screen.Home(""))
        navBackStack.remove(Screen.Splash)
    })
}
