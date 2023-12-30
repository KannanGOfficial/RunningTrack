package com.kannan.runningtrack.utils.notifications.channel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.StringRes
import com.kannan.runningtrack.R

enum class NotificationChannels(
    @StringRes
    val channelId: Int,
    @StringRes
    val channelName: Int,
    @StringRes
    val channelDescription: Int,
    val channelImportance: Int,
    val notificationId : Int
) {
    TRACKING_NOTIFICATION_CHANNEL(
        channelId = R.string.tracking_notification_channel_id,
        channelName = R.string.title_tracking_notification,
        channelDescription = R.string.desc_tracking_notification,
        channelImportance = NotificationManager.IMPORTANCE_DEFAULT,
        notificationId = 100
        );

    fun getNotificationChannel(context: Context) : NotificationChannel =
        NotificationChannel(
            context.getString(channelId),
            context.getString(channelName),
            channelImportance
        ).also {
            it.description = context.getString(channelDescription)
        }

    fun getChannelId(context: Context) =
        context.getString(channelId)
}