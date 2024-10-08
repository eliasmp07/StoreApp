import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.provider.ProviderFactory

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.android.hilt.plugin)
    id("kotlin-parcelize")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.serialization)
}

android {
    namespace = "com.devdroid07.storeapp"
    compileSdk = 34
    val providers: ProviderFactory = project.providers

    defaultConfig {
        applicationId = "com.devdroid07.storeapp"
        minSdk = 24
        targetSdk = 34
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
    flavorDimensions += "env"
    productFlavors {
        create("develop") {
            dimension = "env"
            buildConfigField("String", "BASE_URL", "\"http://192.168.1.81:3000/v1/\"")
            buildConfigField("String", "MERCADOPAGO_URL", "\"https://api.mercadopago.com/\"")
            buildConfigField("String", "COPOMEX_URL", "\"https://api.copomex.com/query/\"")
            val apiKeyMercado = gradleLocalProperties(rootDir, providers).getProperty("API_KEY_DEVELOP_MERCADOPAGO")
            val apiKeyCopomex = gradleLocalProperties(rootDir, providers).getProperty("API_KEY_DEVELOP_COPOMEX")
            buildConfigField("String", "API_KEY_MERCADOPAGO", "\"$apiKeyMercado\"")
            buildConfigField("String", "API_KEY_COPOMEX", "\"$apiKeyCopomex\"")
        }

        create("production") {
            dimension = "env"
            val baseUrl = gradleLocalProperties(rootDir, providers).getProperty("BASE_URL")
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
            buildConfigField("String", "MERCADOPAGO_URL", "\"https://api.mercadopago.com/\"")
            buildConfigField("String", "COPOMEX_URL", "\"https://api.copomex.com/query/\"")
            val apiKeyMercado = gradleLocalProperties(rootDir, providers).getProperty("API_KEY_PRODUCTION_MERCADOPAGO")
            val apiKeyCopomex = gradleLocalProperties(rootDir, providers).getProperty("API_KEY_PRODUCTION_COPOMEX")
            buildConfigField("String", "API_KEY_MERCADOPAGO", "\"$apiKeyMercado\"")
            buildConfigField("String", "API_KEY_COPOMEX", "\"$apiKeyCopomex\"")
        }
    }


    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Get day of week api 25 or lower
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.msz.progress.indicator)

    implementation(libs.coil.compose)
    //Serialization
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.commons.io)

    implementation(libs.androidx.core.splashscreen)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.compiler.android)
    implementation(libs.hilt.android.testing)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.androidx.compose.material.icon)
    implementation(libs.androidx.datastore)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}