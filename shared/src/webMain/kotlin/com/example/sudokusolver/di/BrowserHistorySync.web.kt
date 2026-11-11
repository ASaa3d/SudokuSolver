package com.example.sudokusolver.di

import androidx.compose.runtime.*
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.sudokusolver.navigation.Screen
import com.example.sudokusolver.navigation.toUrlPath
import kotlinx.browser.window
import org.w3c.dom.events.Event

private class HistoryTracker {
    var depth = 1          // number of history entries we believe exist
    var ignorePop = 0      // popstate events caused by our own history.go()
}

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
fun BrowserHistorySync(backStack: NavBackStack<NavKey>) {
    val tracker = remember { HistoryTracker() }

    // Browser Back/Forward pressed -> pop our back stack
    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = {
            if (tracker.ignorePop > 0) {
                tracker.ignorePop--
            } else {
                tracker.depth = (tracker.depth - 1).coerceAtLeast(1)
                if (backStack.size > 1) backStack.removeLastOrNull()
            }
        }
        window.addEventListener("popstate", listener)
        onDispose { window.removeEventListener("popstate", listener) }
    }

    LaunchedEffect(backStack) {
        snapshotFlow { backStack.size to backStack.lastOrNull() }.collect { (size, top) ->
            val path = (top as? Screen)?.toUrlPath() ?: "/"
            when {
                size > tracker.depth -> {
                    repeat(size - tracker.depth - 1) {
                        window.history.pushState(null, "", window.location.href)
                    }
                    window.history.pushState(null, "", path)
                    tracker.depth = size
                }
                size < tracker.depth -> {
                    val diff = tracker.depth - size
                    tracker.ignorePop = 1
                    tracker.depth = size
                    window.history.go(-diff)
                    // note: URL is whatever entry we land on
                }
                else -> {
                    // same size, different top screen: replaced in place
                    window.history.replaceState(null, "", path)
                }
            }
        }
    }
}