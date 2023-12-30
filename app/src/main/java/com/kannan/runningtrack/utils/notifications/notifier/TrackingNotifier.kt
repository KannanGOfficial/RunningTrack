package com.kannan.runningtrack.utils.notifications.notifier

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.kannan.runningtrack.R
import com.kannan.runningtrack.core.presentation.activity.MainActivity
import com.kannan.runningtrack.utils.notifications.channel.NotificationChannels

class TrackingNotifier(private val context : Context): NotifierImpl(context) {

    override val notificationId: Int = NotificationChannels.TRACKING_NOTIFICATION_CHANNEL.channelId

    override val notification: NotificationCompat.Builder =
        NotificationCompat.Builder(context, NotificationChannels.TRACKING_NOTIFICATION_CHANNEL.getChannelId(context))
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setContentTitle(context.getString(R.string.tracking_notification_content_title))
            .setContentIntent(getPendingIntent())

    private fun getPendingIntent() = NavDeepLinkBuilder(context)
        .setComponentName(MainActivity::class.java)
        .setGraph(R.navigation.core_nav_graph)
        .setDestination(R.id.trackingFragment)
        .createPendingIntent()

}