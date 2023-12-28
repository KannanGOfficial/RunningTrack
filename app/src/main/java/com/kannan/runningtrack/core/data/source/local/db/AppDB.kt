package com.kannan.runningtrack.core.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kannan.runningtrack.core.data.source.local.dao.RunDao
import com.kannan.runningtrack.core.data.source.local.entity.RunEntity

@Database(
    entities = [RunEntity::class],
    version = 1
)
abstract class AppDB : RoomDatabase() {

    abstract val runDao : RunDao
}

object AppDatabase{
    const val NAME = "running_track.db"
}