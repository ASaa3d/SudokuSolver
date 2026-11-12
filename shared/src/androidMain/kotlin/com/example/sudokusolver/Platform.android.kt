package com.example.sudokusolver

import android.os.Build
import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.compose.rememberCameraPickerLauncher

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val showSplash: Boolean = true

@Composable
actual fun rememberPlatformCameraLauncher(
    onResult: (PlatformFile?) -> Unit
): () -> Unit {
    val launcher = rememberCameraPickerLauncher { file ->
        onResult(file)
    }
    return { launcher.launch() }
}