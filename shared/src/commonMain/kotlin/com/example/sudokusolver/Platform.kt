package com.example.sudokusolver

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile


interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val showSplash: Boolean

@Composable
expect fun rememberPlatformCameraLauncher(
    onResult: (PlatformFile?) -> Unit
): () -> Unit