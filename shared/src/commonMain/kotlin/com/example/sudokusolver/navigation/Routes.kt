package com.example.sudokusolver.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass


@Serializable
sealed interface Route: NavKey
@Serializable
data object Splash: Route

@Serializable
data object Home: Route

//@Serializable
//data class Details(val itemId: String): Destination


// for web, since it doesn't use reflection
val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Splash::class,Splash.serializer())
            subclass(Home::class,Home.serializer())
        }
    }
}


//@Composable
