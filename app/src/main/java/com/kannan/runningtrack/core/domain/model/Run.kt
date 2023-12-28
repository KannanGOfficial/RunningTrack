package com.kannan.runningtrack.core.domain.model

data class Run(
    val id : String,
    val imageInByteArray : ByteArray,
    val timeStamp : Long,
    val avgSpeedInKMPH : Float,
    val distanceInMeters : Int,
    val runningTimeInMillis : Long,
    val caloriesBurned : Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Run

        if (id != other.id) return false
        if (!imageInByteArray.contentEquals(other.imageInByteArray)) return false
        if (timeStamp != other.timeStamp) return false
        if (avgSpeedInKMPH != other.avgSpeedInKMPH) return false
        if (distanceInMeters != other.distanceInMeters) return false
        if (runningTimeInMillis != other.runningTimeInMillis) return false
        if (caloriesBurned != other.caloriesBurned) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + imageInByteArray.contentHashCode()
        result = 31 * result + timeStamp.hashCode()
        result = 31 * result + avgSpeedInKMPH.hashCode()
        result = 31 * result + distanceInMeters
        result = 31 * result + runningTimeInMillis.hashCode()
        result = 31 * result + caloriesBurned
        return result
    }
}