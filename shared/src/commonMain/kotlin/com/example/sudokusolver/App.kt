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
import com.example.sudokusolver.navigation.Home
import com.example.sudokusolver.navigation.Splash
import com.example.sudokusolver.navigation.navConfig
import com.example.sudokusolver.splash.SplashScreen
import com.example.sudokusolver.sudoku.SudokuViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.compose_multiplatform

@Composable
@Preview
fun App(
    sudokuViewModel: SudokuViewModel = koinViewModel()
) {
    val backStack = rememberNavBackStack(navConfig, Splash)


    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Splash> {
                    SplashScreen(
                        onFinished = {
                            // Replace splash with home so Back doesn't return to it
                            backStack.add(Home)
                            backStack.remove(Splash)
                        }
                    )
                }
                entry<Home> {
//                HomeScreen()
                }
            }
        )
//        Column(
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.primaryContainer)
//                .safeContentPadding()
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//
//           if(sudokuViewModel.recState) {
//               Text(
//                   text = "we can see sudoku c++ code",
//               )
//           }
//
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
    }
}