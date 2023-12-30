package com.kannan.runningtrack.utils.notifications.notifier

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat


abstract class Notifier(context: Context) {

    abstract val notificationBuilder: NotificationCompat.Builder

    abstract val notificationId: Int

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun postNotification() {
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    fun postNotification(contextText: String) {
        notificationManager.notify(
            notificationId,
            notificationBuilder.setContentText(contextText).build()
        )
    }
}