package com.kannan.runningtrack.core.di

import android.content.Context
import androidx.room.Room
import com.kannan.runningtrack.core.data.source.local.db.AppDB
import com.kannan.runningtrack.core.data.source.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideAppDB(@ApplicationContext context: Context): AppDB =
        Room.databaseBuilder(
            context = context,
            klass = AppDB::class.java,
            name = AppDatabase.NAME
        ).fallbackToDestructiveMigration().build()
}