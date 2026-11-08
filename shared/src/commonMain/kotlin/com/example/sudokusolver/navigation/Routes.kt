package com.example.sudokusolver.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


@Serializable
sealed interface Screen : NavKey {
    @Serializable data object Splash : Screen
    @Serializable data object Decider : Screen
    @Serializable data object Login : Screen
    @Serializable data object SignUp : Screen

    @Serializable data class VerifyAccount(val email: String) : Screen
    @Serializable data object ResetAccountEntry : Screen
    @Serializable data class ResetAccountCode(val email: String) : Screen
    @Serializable data class ResetPassword(val email: String) : Screen
    @Serializable data class Home(val email: String) : Screen
}



// for web, since it doesn't use reflection
val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Screen.Splash::class,Screen.Splash.serializer())
            subclass(Screen.Decider::class,Screen.Decider.serializer())
            subclass(Screen.Home::class,Screen.Home.serializer())
        }
    }
}