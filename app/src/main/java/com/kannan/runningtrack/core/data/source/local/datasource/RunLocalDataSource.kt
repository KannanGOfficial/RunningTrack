package com.kannan.runningtrack.core.data.source.local.datasource

import com.kannan.runningtrack.core.data.source.local.entity.RunEntity
import kotlinx.coroutines.flow.Flow

interface RunLocalDataSource {
    suspend fun insertRun(runEntity: RunEntity)
    suspend fun deleteRun(runEntity: RunEntity)
    fun getAllRunSortedByDate() : Flow<RunEntity>
    fun getAllRunSortedByAvgSpeed() : Flow<RunEntity>
    fun getAllRunSortedByDistance() : Flow<RunEntity>
    fun getAllRunSortedByRunningTime() : Flow<RunEntity>
    fun getAllRunSortedByCaloriesBurned() : Flow<RunEntity>
    fun getTotalTimeRunInMillis() : Flow<Long>
    fun getTotalCaloriesBurned() : Flow<Int>
    fun getTotalDistance() : Flow<Int>
    fun getAvgSpeed() : Flow<Float>

}