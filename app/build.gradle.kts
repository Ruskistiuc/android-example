plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.android.junit5)
    id("kotlin-kapt")
}

android {
    compileSdk = 34

    android {
        defaultConfig {
            applicationId = "com.example.androidexample"
            minSdk = 26
            targetSdk = 34
            versionCode = 1
            versionName = "1.0"

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
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
        compose = true
    }

    composeOptions {
        /**
         * Compatibility map of the library that matches the project’s Kotlin version
         * https://developer.android.com/jetpack/androidx/releases/compose-kotlin
         * */
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions.unitTests.isReturnDefaultValues = true
    namespace = "com.example.androidexample"
}

dependencies {
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(libs.core.ktx)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)

    implementation(libs.accompanist.swiperefresh)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.adapter.rxjava3)

    implementation(libs.rxjava)
    implementation(libs.runtime.rxjava)

    implementation(libs.rxandroid)

    testImplementation(libs.mockito.kotlin)

    testImplementation(libs.assertj.joda.time)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)

    androidTestImplementation(libs.junit.jupiter.api)
    androidTestImplementation(libs.android.test.compose)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    debugImplementation(libs.ui.test.manifest)

    debugImplementation(libs.ui.tooling)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
