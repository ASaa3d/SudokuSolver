package com.example.sudokusolver

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.example.sudokusolver.di.initKoin
import kotlinx.browser.document
import kotlinx.browser.window
import androidx.navigation3.runtime.deeplink.DeepLinkRequest

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    val initialTarget = window.location.pathname + window.location.search
    val request = DeepLinkRequest(uri = window.location.href)
    ComposeViewport(document.body!!) {
        App(deepLinkRequest = request, initialTarget = initialTarget)
    }
}