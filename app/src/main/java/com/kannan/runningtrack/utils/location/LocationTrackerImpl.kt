package com.kannan.runningtrack.utils.location

import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.kannan.runningtrack.utils.extensions.checkHasGPSEnabled
import com.kannan.runningtrack.utils.extensions.checkHasLocationPermission
import com.kannan.runningtrack.utils.extensions.checkHasNetworkEnabled
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

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

    override fun getLocationUpdates(): Flow<Location> = callbackFlow {

        if (!context.checkHasLocationPermission())
            throw LocationTracker.LocationException(LocationExceptionEnum.LOCATION_NOT_ENABLED.name)

        if (!context.checkHasGPSEnabled())
            throw LocationTracker.LocationException(LocationExceptionEnum.GPS_NOT_ENABLED.name)

        if (!context.checkHasNetworkEnabled())
            throw LocationTracker.LocationException(LocationExceptionEnum.NETWORK_NOT_ENABLED.name)

        val locationCallBack = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.lastOrNull()?.let {location ->
                    launch { send(location) }
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequestBuilder.build(), locationCallBack, Looper.getMainLooper())

        awaitClose{ fusedLocationProviderClient.removeLocationUpdates(locationCallBack) }
    }

    override fun onLocationResult(result: LocationResult) {
        locationHandlerResult.invoke(LocationTrackerResult.Success(result))
    }
}