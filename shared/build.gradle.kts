import com.android.utils.TraceUtils.simpleId
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.codingfeline.buildkonfig)
}

val localProps = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use{ data -> load(data)}
}

fun config(key:String) : String = System.getenv(key)
        ?: localProps.getProperty(key)
        ?: "https://missing-$key.invalid/".also {
            logger.warn("WARNING: $key not set. Add it to local.properties")
        }

buildkonfig {
    packageName = "com.example.sudokusolver"
    defaultConfigs {
        buildConfigField(STRING, "BASE_URL", config("BASE_URL_DEV"))
        buildConfigField(BOOLEAN, "DEBUG", "true")
    }
    defaultConfigs("prod") {
        buildConfigField(STRING, "BASE_URL", config("BASE_URL_PROD"))
        buildConfigField(BOOLEAN, "DEBUG", "false")
    }
    defaultConfigs("dev") {
        buildConfigField(STRING, "BASE_URL", config("BASE_URL_DEV"))
    }
}

kotlin {
    js {
        browser()
        binaries.executable()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    android {
       namespace = "com.example.sudokusolver.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.material3.icons.extended)
            implementation(libs.datastore.core)
            implementation(libs.datastore.pref)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.compose.material3.adaptive)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
            implementation(libs.ktor.client.js)
        }
        getByName("androidDeviceTest").dependencies {
            implementation(libs.androidx.testRunner)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}