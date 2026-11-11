package com.example.sudokusolver.di

import androidx.compose.runtime.*
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.sudokusolver.navigation.Screen
import com.example.sudokusolver.navigation.toUrlPath
import kotlinx.browser.window
import org.w3c.dom.events.Event

private class HistoryTracker {
    var index = 0                          // current history position (0-based)
    var ignorePop = 0                      // popstate events caused by our own history.go()
    val entries = mutableListOf<NavKey>()  // entries[i] = screen at history index i (keeps forward entries)
}

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
fun BrowserHistorySync(backStack: NavBackStack<NavKey>) {
    val tracker = remember { HistoryTracker() }

    // Browser Back/Forward -> update our back stack
    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = {
            if (tracker.ignorePop > 0) {
                tracker.ignorePop--
            } else {
                val target = (window.history.state as? JsNumber)?.toInt() ?: 0
                val current = tracker.index

                when {
                    target < current -> {                       // Back
                        tracker.index = target                  // set BEFORE touching the stack
                        while (backStack.size > target + 1) backStack.removeLastOrNull()
                    }
                    target > current -> {                       // Forward
                        val screens = (backStack.size..target).map { tracker.entries.getOrNull(it) }
                        if (screens.all { it != null }) {
                            tracker.index = target
                            screens.forEach { backStack.add(it!!) }
                        } else {
                            // forward entry we have no screen for (e.g. after a reload): bounce back
                            tracker.ignorePop = 1
                            tracker.index = backStack.size - 1
                            window.history.go(tracker.index - target)
                        }
                    }
                }
            }
        }
        window.addEventListener("popstate", listener)
        onDispose { window.removeEventListener("popstate", listener) }
    }

    // In-app navigation -> update browser history
    LaunchedEffect(backStack) {
        snapshotFlow { backStack.size to backStack.lastOrNull() }.collect { (size, top) ->
            if (top == null || top is Screen.Splash) return@collect   // keep the incoming URL while Splash resolves it

            val path = (top as? Screen)?.toUrlPath() ?: "/"
            val targetIndex = size - 1

            when {
                targetIndex > tracker.index -> {                // app pushed
                    for (i in (tracker.index + 1)..targetIndex) {
                        val url = if (i == targetIndex) path else window.location.href
                        window.history.pushState(i.toJsNumber(), "", url)
                    }
                    tracker.index = targetIndex
                    tracker.entries.clear()                     // a push discards forward history
                    tracker.entries.addAll(backStack)
                }
                targetIndex < tracker.index -> {                // app popped (in-app back button)
                    val diff = tracker.index - targetIndex
                    tracker.ignorePop = 1
                    tracker.index = targetIndex
                    window.history.go(-diff)                    // forward entries stay in tracker.entries
                }
                else -> {                                       // replaced in place (Splash -> real screen) or Back/Forward settled
                    if (tracker.entries.size > targetIndex) tracker.entries[targetIndex] = top
                    else { tracker.entries.clear(); tracker.entries.addAll(backStack) }
                    window.history.replaceState(targetIndex.toJsNumber(), "", path)
                }
            }
        }
    }
}