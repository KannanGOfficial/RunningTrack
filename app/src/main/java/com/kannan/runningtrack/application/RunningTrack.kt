package com.kannan.runningtrack.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RunningTrack : Application() {

    override fun onCreate() {
        super.onCreate()

        plantTimber()
    }

    private fun plantTimber(){
        Timber.plant(Timber.DebugTree())
    }
}