package com.kannan.runningtrack.core.data.repository

import com.kannan.runningtrack.core.data.source.local.datasource.RunDataSource
import com.kannan.runningtrack.core.data.source.local.entity.RunEntity
import com.kannan.runningtrack.core.data.source.local.mappers.toRun
import com.kannan.runningtrack.core.domain.mappers.toEntity
import com.kannan.runningtrack.core.domain.model.Run
import com.kannan.runningtrack.core.domain.repository.RunRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RunRepositoryImpl @Inject constructor(
    private val localDataSource: RunDataSource
) : RunRepository {
    override suspend fun insertRun(run: Run) =
        localDataSource.insertRun(run.toEntity())
    override suspend fun deleteRun(run: Run) =
        localDataSource.deleteRun(run.toEntity())

    override fun getAllRunSortedByDate(): Flow<Run> =
        localDataSource.getAllRunSortedByDate().map(RunEntity::toRun)

    override fun getAllRunSortedByAvgSpeed(): Flow<Run> =
        localDataSource.getAllRunSortedByAvgSpeed().map(RunEntity::toRun)

    override fun getAllRunSortedByDistance(): Flow<Run> =
        localDataSource.getAllRunSortedByDistance().map(RunEntity::toRun)

    override fun getAllRunSortedByRunningTime(): Flow<Run> =
        localDataSource.getAllRunSortedByRunningTime().map(RunEntity::toRun)

    override fun getAllRunSortedByCaloriesBurned(): Flow<Run> =
        localDataSource.getAllRunSortedByCaloriesBurned().map(RunEntity::toRun)

    override fun getTotalTimeRunInMillis(): Flow<Long> =
        localDataSource.getTotalTimeRunInMillis()
    override fun getTotalCaloriesBurned(): Flow<Int> =
        localDataSource.getTotalCaloriesBurned()

    override fun getTotalDistance(): Flow<Int> =
        localDataSource.getTotalDistance()

    override fun getAvgSpeed(): Flow<Float> =
        localDataSource.getAvgSpeed()
}