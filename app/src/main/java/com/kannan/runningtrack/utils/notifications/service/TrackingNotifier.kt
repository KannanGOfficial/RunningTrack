package com.kannan.runningtrack.utils.notifications.service

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.kannan.runningtrack.R
import com.kannan.runningtrack.utils.notifications.channel.NotificationChannels

class TrackingNotifier(context : Context): Notifier(context) {

    override val notificationId: Int = NotificationChannels.TRACKING_NOTIFICATION_CHANNEL.channelId

    override val notification: NotificationCompat.Builder =
        NotificationCompat.Builder(context, NotificationChannels.TRACKING_NOTIFICATION_CHANNEL.getChannelId(context))
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setContentTitle(context.getString(R.string.tracking_notification_content_title))
            .setProgress(100, 10, true)

}