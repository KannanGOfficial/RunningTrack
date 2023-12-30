package com.kannan.runningtrack.utils.notifications.service

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat


abstract class Notifier(context : Context) {

    abstract val notification : NotificationCompat.Builder

    abstract val notificationId : Int

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(){
        notificationManager.notify(notificationId,notification.build())
    }
}