package com.kannan.runningtrack.application

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import com.kannan.runningtrack.utils.notifications.channel.NotificationChannels
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RunningTrack : Application() {

    override fun onCreate() {
        super.onCreate()

        plantTimber()
        setupNotificationChannel()
    }

    private fun setupNotificationChannel() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannels(
            NotificationChannels.entries.map {
                it.getNotificationChannel(applicationContext)
            }
        )
    }

    private fun plantTimber(){
        Timber.plant(Timber.DebugTree())
    }
}