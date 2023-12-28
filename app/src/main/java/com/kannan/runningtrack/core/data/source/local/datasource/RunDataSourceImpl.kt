package com.kannan.runningtrack.core.data.source.local.datasource

import com.kannan.runningtrack.core.data.source.local.db.AppDB
import com.kannan.runningtrack.core.data.source.local.entity.RunEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RunDataSourceImpl @Inject constructor(
    private val appDB : AppDB
) : RunDataSource {
    override suspend fun insertRun(runEntity: RunEntity) =
        appDB.runDao.insertRun(runEntity)

    override suspend fun deleteRun(runEntity: RunEntity) =
        appDB.runDao.deleteRun(runEntity)

    override fun getAllRunSortedByDate(): Flow<RunEntity> =
        appDB.runDao.getAllRunSortedByDate()

    override fun getAllRunSortedByAvgSpeed(): Flow<RunEntity> =
        appDB.runDao.getAllRunSortedByAvgSpeed()

    override fun getAllRunSortedByDistance(): Flow<RunEntity> =
        appDB.runDao.getAllRunSortedByDistance()

    override fun getAllRunSortedByRunningTime(): Flow<RunEntity> =
        appDB.runDao.getAllRunSortedByRunningTime()

    override fun getAllRunSortedByCaloriesBurned(): Flow<RunEntity> =
        appDB.runDao.getAllRunSortedByCaloriesBurned()

    override fun getTotalTimeRunInMillis(): Flow<Long> =
        appDB.runDao.getTotalTimeRunInMillis()

    override fun getTotalCaloriesBurned(): Flow<Int> =
        appDB.runDao.getTotalCaloriesBurned()

    override fun getTotalDistance(): Flow<Int> =
        appDB.runDao.getTotalDistance()

    override fun getAvgSpeed(): Flow<Float> =
        appDB.runDao.getAvgSpeed()
}