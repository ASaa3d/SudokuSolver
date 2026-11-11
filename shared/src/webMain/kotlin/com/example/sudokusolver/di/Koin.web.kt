package com.example.sudokusolver.di

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.sudokusolver.di.di.webModule
import org.koin.core.module.Module

actual val platformModule: Module = webModule

@Composable
actual fun PlatformBackSync(backStack: NavBackStack<NavKey>) = BrowserHistorySync(backStack)