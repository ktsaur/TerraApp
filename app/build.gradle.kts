plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
//    alias(libs.plugins.google.services)
    kotlin("plugin.serialization") version "2.0.21"
}

android {
    namespace = "ru.itis.terraapp"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.itis.terraapp"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //Core
    implementation(project(path=":core:base"))
    implementation(project(path=":core:data"))
    implementation(project(path=":core:domain"))

    //Feature
    implementation(project(path=":feature:auth"))
    implementation(project(path=":feature:attractions"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material)
    implementation(libs.collections)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.fragment)

    //Hilt
    implementation(libs.hilt)
    implementation(libs.hilt.navigation)
    ksp(libs.hilt.compiler)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.converter.gson)

    //Compose Navigation
    implementation(libs.androidx.navigation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.foundation)

    //Room
    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.ksp)
}