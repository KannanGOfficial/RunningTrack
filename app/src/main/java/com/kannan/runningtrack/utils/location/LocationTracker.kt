package com.kannan.runningtrack.utils.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationTracker{
    fun startLocationTracking()
    fun stopLocationTracking()

    val locationUpdatesFlow : Flow<Location>
}