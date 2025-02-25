
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("de.jensklingenberg.ktorfit")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.compose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.compose"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.rules)
    implementation(libs.androidx.recyclerview)
//    implementation(libs.androidx.ui.test.junit4.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.junit)
//    androidTestImplementation(libs.junit.jupiter)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    androidTestImplementation( libs.mockito.android)
    androidTestImplementation( libs.mockito.kotlin)
    androidTestImplementation (libs.mockk.android)

    androidTestImplementation(libs.androidx.espresso.web)
    androidTestImplementation(libs.androidx.espresso.core)

//    androidTestImplementation(libs.allure.kotlin.android)
    androidTestImplementation(libs.allure.junit4)

    implementation(libs.ktorfitlib)
//    implementation(libs.googleksp)
    implementation(libs.ktor.client.core)
//    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
//    implementation(libs.ktor.client.plugins)
    implementation(libs.ktor.client.plugins.v302)
//    implementation(libs.dagger.compiler)
//    ksp(libs.dagger.compiler)

    implementation (libs.socket.io.client)

    implementation(libs.json)


}