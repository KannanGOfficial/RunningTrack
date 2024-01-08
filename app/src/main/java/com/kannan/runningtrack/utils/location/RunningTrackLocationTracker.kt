package com.kannan.runningtrack.utils.location

import android.content.Context
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority

class RunningTrackLocationTracker(
    context: Context,
) : LocationTrackerImpl(context) {

    override val locationRequestBuilder: LocationRequest.Builder =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L).apply {
            setMinUpdateIntervalMillis(2000L)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }
    override val timberTag: String = RunningTrackLocationTracker::class.java.simpleName

}