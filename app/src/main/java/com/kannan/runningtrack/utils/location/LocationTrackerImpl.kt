package com.kannan.runningtrack.utils.location

import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.kannan.runningtrack.utils.extensions.checkHasLocationPermission

abstract class LocationTrackerImpl(
    private val context: Context,
    private val locationHandlerResult: ((LocationTrackerResult) -> Unit)
) : LocationCallback(), LocationTracker {


    abstract val locationRequestBuilder: LocationRequest.Builder

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    override fun startLocationTracking() {
        try {
            if (context.checkHasLocationPermission()) {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequestBuilder.build(),
                    this,
                    Looper.getMainLooper()
                )
            } else {
                locationHandlerResult.invoke(LocationTrackerResult.Error("Error :Location Permission is not Available"))
            }
        } catch (e: Exception) {
            locationHandlerResult.invoke(LocationTrackerResult.Error("Error : $e"))
        }
    }

    override fun stopLocationTracking() {
        try {
            fusedLocationProviderClient.flushLocations()
            fusedLocationProviderClient.removeLocationUpdates(this)
        } catch (e: Exception) {
            locationHandlerResult.invoke(LocationTrackerResult.Error("Error : $e"))
        }
    }

    override fun onLocationResult(result: LocationResult) {
        locationHandlerResult.invoke(LocationTrackerResult.Success(result))
    }
}