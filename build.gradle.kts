// Top-level build file where you can add configuration options common to all sub-projects/modules.
@file:Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.gradle) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.map.secrets.plugin) apply false
}

buildscript {
    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin.classpath)
        classpath(libs.gradle.classpath)
        classpath(libs.secrets.gradle.plugin.classpath)
    }
}
