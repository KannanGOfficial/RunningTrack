package com.kannan.runningtrack.utils.extensions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.kannan.runningtrack.utils.permissions.AppPermission

fun Context.checkHasPermission(permission: AppPermission) =
    ActivityCompat.checkSelfPermission(this,permission.value) == PackageManager.PERMISSION_GRANTED