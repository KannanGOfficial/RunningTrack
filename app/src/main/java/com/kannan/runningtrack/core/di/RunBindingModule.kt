package com.kannan.runningtrack.core.di

import com.kannan.runningtrack.core.data.repository.RunRepositoryImpl
import com.kannan.runningtrack.core.data.source.local.datasource.RunLocalDataSource
import com.kannan.runningtrack.core.data.source.local.datasource.RunLocalDataSourceImpl
import com.kannan.runningtrack.core.domain.repository.RunRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RunBindingModule {

    @Binds
    @Singleton
    fun bindRunRepository(runRepository : RunRepositoryImpl) : RunRepository

    @Binds
    @Singleton
    fun bindRunLocalDataSource(runLocalDataSource: RunLocalDataSourceImpl) : RunLocalDataSource
}