package com.example.sudokusolver.di

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

private var koinRef: Koin? = null
expect val platformModule: Module

fun getKoin(): Koin = koinRef?: throw Throwable("Koin is not initialized. Call initKoin() first.")

// commonMain
@Composable
expect fun PlatformBackSync(backStack: NavBackStack<NavKey>)

fun initKoin(config: (KoinApplication) -> Unit = {})
{
    if (koinRef == null) {
        val app = startKoin {
            config(this)
            modules(commonModule, platformModule)
        }

        koinRef = app.koin
    }
}
