package com.example.sudokusolver.splash.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.sudokusolver.splash.domain.model.ParsedUrl
import core.BaseViewModel
import com.example.sudokusolver.splash.domain.usecase.RefreshUseCase
import com.example.sudokusolver.splash.presentation.event.SplashEvent
import com.example.sudokusolver.splash.presentation.state.SplashState
import kotlinx.coroutines.launch

class SplashViewModel(
    private val refreshUseCase: RefreshUseCase,
) : BaseViewModel<SplashState, SplashEvent>(SplashState()) {



    fun refresh() {
        viewModelScope.launch {
            updateState { currentState.copy(isLoading = true, error = null) }
            refreshUseCase()
                .onSuccess { email ->
                    sendEvent(SplashEvent.ResolveTarget(email))
                }
                .onFailure {
                    sendEvent(SplashEvent.ResolveTarget(null))
                }
            updateState { currentState.copy(isLoading = false) }
        }
    }

    fun resolveTarget(target: String?, email: String?) {
        val isSignedIn = !email.isNullOrBlank()
        val userEmail = email.orEmpty()

        if (target.isNullOrBlank() || target == "/") {
            if (isSignedIn) {
                sendEvent(SplashEvent.NavigateToHome(userEmail))
            } else {
                sendEvent(SplashEvent.NavigateToLogin)
            }
            return
        }

        val parsed = parseUrl(target)
        val path = parsed.path
        val params = parsed.queryParams

        when (path) {
            "/login" -> {
                if (isSignedIn) {
                    sendEvent(SplashEvent.NavigateToHome(userEmail))
                } else {
                    sendEvent(SplashEvent.NavigateToLogin)
                }
            }

            "/signup" -> {
                if (isSignedIn) {
                    sendEvent(SplashEvent.NavigateToHome(userEmail))
                } else {
                    sendEvent(SplashEvent.NavigateToSignUp)
                }
            }

            "/home" -> {
                val targetEmail = params["email"]
                val isGuest = params["isGuest"]?.toBoolean() ?: false
                if (isSignedIn) {
                    sendEvent(SplashEvent.NavigateToHome(userEmail, isGuest = false))
                } else {
                    if (isGuest) {
                        sendEvent(
                            SplashEvent.NavigateToHome(
                                targetEmail ?: "guest@user.com",
                                isGuest = true
                            )
                        )
                    } else {
                        sendEvent(SplashEvent.NavigateToLogin)
                    }
                }
            }

            "/verify-account" -> {
                val targetEmail = params["email"]
                val targetCode = params["code"]
                if (!targetEmail.isNullOrBlank() && !targetCode.isNullOrBlank()) {
                    sendEvent(SplashEvent.NavigateToVerifyAccount(targetEmail, targetCode))
                } else {
                    if (isSignedIn) {
                        sendEvent(SplashEvent.NavigateToHome(userEmail))
                    } else {
                        sendEvent(SplashEvent.NavigateToLogin)
                    }
                }
            }

            "/reset-email" -> {
                if (isSignedIn) {
                    sendEvent(SplashEvent.NavigateToHome(userEmail))
                } else {
                    sendEvent(SplashEvent.NavigateToResetAccountEmail)
                }
            }

            "/verify", "/reset-code" -> {
                val targetEmail = params["email"]
                val isVerifyAccount = params["isVerifyAccount"]?.toBoolean() ?: false
                if (isSignedIn) {
                    sendEvent(SplashEvent.NavigateToHome(userEmail))
                } else if (!targetEmail.isNullOrBlank()) {
                    sendEvent(SplashEvent.NavigateToResetAccountCode(targetEmail, isVerifyAccount))
                } else {
                    sendEvent(SplashEvent.NavigateToLogin)
                }
            }

            "/reset", "/reset-password" -> {
                val targetEmail = params["email"]
                val targetCode = params["code"]
                if (!targetEmail.isNullOrBlank() && !targetCode.isNullOrBlank()) {
                    sendEvent(SplashEvent.NavigateToResetPassword(targetEmail, targetCode))
                } else {
                    if (isSignedIn) {
                        sendEvent(SplashEvent.NavigateToHome(userEmail))
                    } else {
                        sendEvent(SplashEvent.NavigateToLogin)
                    }
                }
            }

            else -> {
                if (isSignedIn) {
                    sendEvent(SplashEvent.NavigateToHome(userEmail))
                } else {
                    sendEvent(SplashEvent.NavigateToLogin)
                }
            }
        }
    }


    private fun parseUrl(target: String): ParsedUrl {
        val relative = if (target.contains("://")) {
            val afterScheme = target.substringAfter("://")
            val slashIndex = afterScheme.indexOf('/')
            if (slashIndex != -1) afterScheme.substring(slashIndex) else "/"
        } else {
            target
        }

        val parts = relative.split("?", limit = 2)
        val path = parts[0].trimEnd('/')
        val normalizedPath = if (path.isEmpty()) "/" else path

        val queryParams = mutableMapOf<String, String>()
        if (parts.size > 1 && parts[1].isNotEmpty()) {
            val pairs = parts[1].split("&")
            for (pair in pairs) {
                val kv = pair.split("=", limit = 2)
                if (kv.isNotEmpty()) {
                    val key = kv[0]
                    val value = if (kv.size > 1) decodeValue(kv[1]) else ""
                    queryParams[key] = value
                }
            }
        }
        return ParsedUrl(normalizedPath, queryParams)
    }

    private fun decodeValue(value: String): String {
        return value.replace("%40", "@").replace("%20", " ")
    }
}
