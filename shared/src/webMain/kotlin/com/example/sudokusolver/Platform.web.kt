package com.example.sudokusolver

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile

@Composable
actual fun rememberPlatformCameraLauncher(onResult: (PlatformFile?) -> Unit): () -> Unit {
    return {}
}