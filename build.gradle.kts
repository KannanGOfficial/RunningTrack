// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.devtools.ksp") version ("1.9.0-1.0.13") apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
}

buildscript {
    val navVersion = "2.7.6"
    val androidGradlePlugin = "8.1.1"

    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath("com.android.tools.build:gradle:$androidGradlePlugin")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}
