plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.kannan.runningtrack"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kannan.runningtrack"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding = true
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation(libs.androidx.appcompat)
    implementation("com.google.android.material:material:1.10.0")
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //LifeCycle Service
    implementation(libs.androidx.lifecycle.service)

    //Intuit
    implementation(libs.sdp.android)

    //navigation
    implementation(libs.bundles.navigation.components)

    //room
    implementation(libs.bundles.room)

    //dagger - hilt
    implementation(libs.bundles.dagger.hilt)

    //splash screen
    implementation(libs.splash)

    //glide
    implementation(libs.bundles.glide)

    //Timber
    implementation(libs.timber)

    // Maps SDK for Android
    implementation(libs.bundles.google.maps.location.services)

    //MP Chart
    implementation(libs.mpchart)

}