import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

// For non-secret values like URLs (warns, doesn't fail)
fun config(key: String): String = System.getenv(key)
    ?: localProps.getProperty(key)
    ?: "https://missing-$key.invalid/".also {
        logger.warn("WARNING: $key not set. Add it to local.properties")
    }

// For secrets (fails the build with a clear message)
fun requiredConfig(key: String): String = System.getenv(key)
    ?: localProps.getProperty(key)
    ?: error("$key is not set. Add it to local.properties or as an environment variable.")


plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}
dependencies {
    implementation(project(":shared"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation3.runtime)

    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
}

android {
    namespace = "com.example.sudokusolver"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.sudokuSolver"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(requiredConfig("RELEASE_STORE_FILE"))
            storePassword = requiredConfig("RELEASE_STORE_PASSWORD")
            keyAlias = requiredConfig("RELEASE_KEY_ALIAS")
            keyPassword = requiredConfig("RELEASE_KEY_PASSWORD")
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    androidResources { noCompress += listOf("onnx") }


}