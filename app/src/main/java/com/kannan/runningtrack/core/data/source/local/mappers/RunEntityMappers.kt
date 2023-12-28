package com.kannan.runningtrack.core.data.source.local.mappers

import com.kannan.runningtrack.core.data.source.local.entity.RunEntity
import com.kannan.runningtrack.core.domain.model.Run

fun RunEntity.toRun() = Run(
    id = id,
    imageInByteArray = imageInByteArray,
    timeStamp = timeStamp,
    avgSpeedInKMPH = avgSpeedInKMPH,
    distanceInMeters = distanceInMeters,
    runningTimeInMillis = runningTimeInMillis,
    caloriesBurned = caloriesBurned
)