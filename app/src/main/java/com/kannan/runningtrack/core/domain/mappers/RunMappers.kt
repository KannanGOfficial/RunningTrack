package com.kannan.runningtrack.core.domain.mappers

import com.kannan.runningtrack.core.data.source.local.entity.RunEntity
import com.kannan.runningtrack.core.domain.model.Run

fun Run.toEntity() = RunEntity(
    id = id,
    imageInByteArray = imageInByteArray,
    timeStamp = timeStamp,
    avgSpeedInKMPH = avgSpeedInKMPH,
    distanceInMeters = distanceInMeters,
    runningTimeInMillis = runningTimeInMillis,
    caloriesBurned = caloriesBurned
)