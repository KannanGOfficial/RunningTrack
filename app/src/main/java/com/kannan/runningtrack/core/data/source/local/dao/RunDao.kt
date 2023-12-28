package com.kannan.runningtrack.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.kannan.runningtrack.core.data.source.local.entity.RunEntity
import com.kannan.runningtrack.core.data.source.local.entity.RunTable
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {

    @Upsert
    suspend fun insertRun(runEntity: RunEntity)

    @Delete
    suspend fun deleteRun(runEntity: RunEntity)

    @Query("SELECT * FROM ${RunTable.TABLE_NAME} ORDER BY ${RunTable.Column.TIME_STAMP} DESC")
    fun getAllRunSortedByDate() : Flow<RunEntity>

    @Query("SELECT * FROM ${RunTable.TABLE_NAME} ORDER BY ${RunTable.Column.AVG_SPEED_IN_KMPH} DESC")
    fun getAllRunSortedByAvgSpeed() : Flow<RunEntity>

    @Query("SELECT * FROM ${RunTable.TABLE_NAME} ORDER BY ${RunTable.Column.DISTANCE_IN_METERS} DESC")
    fun getAllRunSortedByDistance() : Flow<RunEntity>

    @Query("SELECT * FROM ${RunTable.TABLE_NAME} ORDER BY ${RunTable.Column.RUNNING_TIME_IN_MILLIS} DESC")
    fun getAllRunSortedByRunningTime() : Flow<RunEntity>

    @Query("SELECT * FROM ${RunTable.TABLE_NAME} ORDER BY ${RunTable.Column.CALORIES_BURNED} DESC")
    fun getAllRunSortedByCaloriesBurned() : Flow<RunEntity>

    @Query("SELECT SUM(${RunTable.Column.TIME_STAMP}) FROM ${RunTable.TABLE_NAME}")
    fun getTotalTimeRunInMillis() : Flow<Long>

    @Query("SELECT SUM(${RunTable.Column.CALORIES_BURNED}) FROM ${RunTable.TABLE_NAME}")
    fun getTotalCaloriesBurned() : Flow<Int>

    @Query("SELECT SUM(${RunTable.Column.DISTANCE_IN_METERS}) FROM ${RunTable.TABLE_NAME}")
    fun getTotalDistance() : Flow<Int>

    @Query("SELECT AVG(${RunTable.Column.AVG_SPEED_IN_KMPH}) FROM ${RunTable.TABLE_NAME}")
    fun getAvgSpeed() : Flow<Float>

}