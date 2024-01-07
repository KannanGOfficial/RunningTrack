package com.kannan.runningtrack.utils.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationTracker{
    fun startLocationTracking()
    fun stopLocationTracking()

    fun getLocationUpdates() : Flow<Location>

    class LocationException(val errorMessage : String) : Exception()
}

interface LocationTrackerFlow{
    fun startLocationTracking()
    fun stopLocationTracking()

    val locationUpdatesFlow : Flow<Location>

//    fun getLocationUpdates() : Flow<Location>

    class LocationException(val errorMessage : String) : Exception()
}

enum class LocationExceptionEnum{
    LOCATION_NOT_ENABLED,
    GPS_NOT_ENABLED,
    NETWORK_NOT_ENABLED
}