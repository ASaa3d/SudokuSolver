package com.example.sudokusolver.di

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module

actual val platformModule: Module = androidModule

@Composable
actual fun PlatformBackSync(backStack: NavBackStack<NavKey>) {}