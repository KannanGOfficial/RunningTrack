package com.kannan.runningtrack.application

import android.app.Application
import timber.log.Timber

class RunningTrack : Application() {

    override fun onCreate() {
        super.onCreate()

        plantTimber()
    }

    private fun plantTimber(){
        Timber.plant(Timber.DebugTree())
    }
}