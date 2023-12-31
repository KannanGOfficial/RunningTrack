package com.kannan.runningtrack.utils.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.kannan.runningtrack.utils.permissions.AppPermission

fun Context.checkHasPermission(permission: AppPermission) =
    ActivityCompat.checkSelfPermission(this,permission.value) == PackageManager.PERMISSION_GRANTED

fun Context.checkHasLocationPermission() =
    checkHasPermission(AppPermission.COARSE_LOCATION) || checkHasPermission(AppPermission.FINE_LOCATION)

fun Context.getLocationManager() = getSystemService(Context.LOCATION_SERVICE) as LocationManager

fun Context.checkHasGPSEnabled() = getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER)

fun Context.checkHasNetworkEnabled() = getLocationManager().isProviderEnabled(LocationManager.NETWORK_PROVIDER)