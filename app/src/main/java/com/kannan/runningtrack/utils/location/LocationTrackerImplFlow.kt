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
import timber.log.Timber

abstract class LocationTrackerImplFlow(
    private val context: Context,
) : LocationTrackerFlow {

    abstract val locationRequestBuilder: LocationRequest.Builder

    abstract val timberTag: String

    private lateinit var locationCallback: LocationCallback

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    override fun startLocationTracking() {

        if (!context.checkHasLocationPermission()) {
            Timber.tag(timberTag).d("Location Permission is Disabled..")
            return
        }

        if (!context.checkHasGPSEnabled()) {
            Timber.tag(timberTag).d("GPS is Disabled...")
            return
        }

        if (!context.checkHasNetworkEnabled()) {
            Timber.tag(timberTag).d("Network Connection is Not Available...")
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequestBuilder.build(),
            locationCallback,
            Looper.getMainLooper()
        )

        Timber.tag(timberTag).d("Locations are Requested...")

    }

    override fun stopLocationTracking() {
        fusedLocationProviderClient.flushLocations()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    override val locationUpdatesFlow: Flow<Location> = callbackFlow {

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.lastOrNull()?.let { location ->
                    Timber.tag(timberTag).d("Location updates are...")
                    launch { send(location) }
                }
            }
        }

        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }
}