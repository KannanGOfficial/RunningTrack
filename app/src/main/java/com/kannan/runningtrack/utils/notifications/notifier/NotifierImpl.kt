package com.kannan.runningtrack.utils.notifications.notifier

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat


abstract class NotifierImpl(context : Context) : Notifier{

    abstract val notification : NotificationCompat.Builder

    abstract val notificationId : Int

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun postNotification(){
        notificationManager.notify(notificationId,notification.build())
    }

    override fun postNotification(contextText: String) {
        notificationManager.notify(
            notificationId,
            notification.setContentText(contextText).build()
        )
    }
}