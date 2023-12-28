package com.kannan.runningtrack.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = RunTable.TABLE_NAME)
data class RunEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = RunTable.Column.ID)
    val id : Int,

    @ColumnInfo(name = RunTable.Column.IMAGE_IN_BYTE_ARRAY)
    val imageInByteArray : ByteArray,

    @ColumnInfo(name = RunTable.Column.TIME_STAMP)
    val timeStamp : Long,

    @ColumnInfo(name = RunTable.Column.AVG_SPEED_IN_KMPH)
    val avgSpeedInKMPH : Float,

    @ColumnInfo(name = RunTable.Column.DISTANCE_IN_METERS)
    val distanceInMeters : Int,

    @ColumnInfo(name = RunTable.Column.RUNNING_TIME_IN_MILLIS)
    val runningTimeInMillis : Long,

    @ColumnInfo(name = RunTable.Column.CALORIES_BURNED)
    val caloriesBurned : Int

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RunEntity

        if (id != other.id) return false
        if (!imageInByteArray.contentEquals(other.imageInByteArray)) return false
        if (timeStamp != other.timeStamp) return false
        if (avgSpeedInKMPH != other.avgSpeedInKMPH) return false
        if (distanceInMeters != other.distanceInMeters) return false
        if (runningTimeInMillis != other.runningTimeInMillis) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + imageInByteArray.contentHashCode()
        result = 31 * result + timeStamp.hashCode()
        result = 31 * result + avgSpeedInKMPH.hashCode()
        result = 31 * result + distanceInMeters
        result = 31 * result + runningTimeInMillis.hashCode()
        return result
    }
}

object RunTable{
    const val TABLE_NAME = "run_table"

    object Column{
        const val ID = "id"
        const val IMAGE_IN_BYTE_ARRAY = "image_in_byte_array"
        const val TIME_STAMP = "time_stamp"
        const val AVG_SPEED_IN_KMPH = "avg_speed_in_kmph"
        const val DISTANCE_IN_METERS = "distance_in_meters"
        const val RUNNING_TIME_IN_MILLIS = "running_time_in_millis"
        const val CALORIES_BURNED = "calories_burned"
    }
}