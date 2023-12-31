package com.kannan.runningtrack.utils.location

import com.google.android.gms.location.LocationResult


sealed interface LocationTrackerResult{
    data class Success(val result : LocationResult) : LocationTrackerResult
    data class Error(val error : String) : LocationTrackerResult
}