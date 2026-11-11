package com.example.sudokusolver.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


@Serializable
sealed interface Screen : NavKey {
    @Serializable data class Splash(val target :String?=null) : Screen
    @Serializable data object Login : Screen
    @Serializable data object SignUp : Screen

    @Serializable data class VerifyAccount(val email: String, val code: String) : Screen
    @Serializable data object ResetAccountEmail : Screen
    @Serializable data class ResetAccountCode(val email: String, val isVerifyAccount: Boolean) : Screen
    @Serializable data class ResetPassword(val email: String, val code:String) : Screen
    @Serializable data class Home(val email: String, val isGuest: Boolean) : Screen
}



// for web, since it doesn't use reflection
val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Screen.Splash::class, Screen.Splash.serializer())
            subclass(Screen.Login::class, Screen.Login.serializer())
            subclass(Screen.SignUp::class, Screen.SignUp.serializer())
            subclass(Screen.VerifyAccount::class, Screen.VerifyAccount.serializer())
            subclass(Screen.ResetAccountEmail::class, Screen.ResetAccountEmail.serializer())
            subclass(Screen.ResetAccountCode::class, Screen.ResetAccountCode.serializer())
            subclass(Screen.ResetPassword::class, Screen.ResetPassword.serializer())
            subclass(Screen.Home::class, Screen.Home.serializer())
        }
    }
}


// Map Screen objects back into URLs for the browser address bar
fun Screen.toUrlPath(): String = when (this) {
    is Screen.Splash -> "/"
    is Screen.Login -> "/login"
    is Screen.SignUp -> "/signup"
    is Screen.VerifyAccount -> "/verify-account"
    is Screen.ResetAccountEmail -> "/reset-email"
    is Screen.ResetPassword -> "/reset-password"
    is Screen.ResetAccountCode -> "/reset-code"
    is Screen.Home -> "/home"
}


