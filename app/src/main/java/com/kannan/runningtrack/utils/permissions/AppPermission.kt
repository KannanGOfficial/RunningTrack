package com.kannan.runningtrack.utils.permissions

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

enum class AppPermission(val value : String) {

    RECORD_AUDIO(Manifest.permission.RECORD_AUDIO),
    WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE),
    READ_EXTERNAL_STORAGE(Manifest.permission.READ_EXTERNAL_STORAGE),
    COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION),
    FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),

    @RequiresApi(Build.VERSION_CODES.Q)
    BACKGROUND_LOCATION(Manifest.permission.ACCESS_BACKGROUND_LOCATION),

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    NOTIFICATION_PERMISSION(Manifest.permission.POST_NOTIFICATIONS)
}

