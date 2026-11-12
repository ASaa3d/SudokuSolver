package com.example.sudokusolver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.deeplink.DeepLinkRequest

class MainActivity : ComponentActivity() {
    private var deepLink by mutableStateOf<DeepLinkRequest?>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            deepLink = intent?.data?.toString()?.let { DeepLinkRequest(uri = it) }        }

        // App already running (singleTop) → link arrives here
        addOnNewIntentListener { newIntent ->
            deepLink = newIntent.data?.toString()?.let { DeepLinkRequest(uri = it) }
        }

        // androidApp MainActivity
        setContent {
            App(deepLinkRequest = deepLink)
        }
    }
}
@Preview
@Composable
fun AppAndroidPreview() {
    App()
}