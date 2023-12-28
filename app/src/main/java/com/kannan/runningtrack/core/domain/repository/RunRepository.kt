package com.kannan.runningtrack.core.domain.repository

import com.kannan.runningtrack.core.domain.model.Run
import kotlinx.coroutines.flow.Flow

interface RunRepository {
    suspend fun insertRun(run : Run)
    suspend fun deleteRun(run: Run)
    fun getAllRunSortedByDate() : Flow<Run>
    fun getAllRunSortedByAvgSpeed() : Flow<Run>
    fun getAllRunSortedByDistance() : Flow<Run>
    fun getAllRunSortedByRunningTime() : Flow<Run>
    fun getAllRunSortedByCaloriesBurned() : Flow<Run>
    fun getTotalTimeRunInMillis() : Flow<Long>
    fun getTotalCaloriesBurned() : Flow<Int>
    fun getTotalDistance() : Flow<Int>
    fun getAvgSpeed() : Flow<Float>
}