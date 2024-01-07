package com.kannan.runningtrack.utils.location

import android.content.Context
import android.location.Location
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.Flow

class RunningTrackLocationTracker(
    context: Context,
    locationHandlerResult: ((LocationTrackerResult) -> Unit)
) : LocationTrackerImplFlow(context) {

    override val locationRequestBuilder: LocationRequest.Builder =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L).apply {
            setMinUpdateIntervalMillis(2000L)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }
    override val timberTag: String = RunningTrackLocationTracker::class.java.simpleName

}