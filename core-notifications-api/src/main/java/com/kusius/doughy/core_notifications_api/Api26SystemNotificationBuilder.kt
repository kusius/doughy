package com.kusius.doughy.core_notifications_api

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.CATEGORY_ALARM
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import com.kusius.doughy.core.notifications.api.NotificationData
import com.kusius.doughy.core.notifications.api.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class Api26SystemNotificationBuilder @Inject constructor(private val context: Context, private val notificationManager: NotificationManager) : SystemNotificationBuilder {
    override fun buildSystemNotification(notificationData: NotificationData): Notification {
        val channel = createChannel(notificationData.channel)
        notificationManager.createNotificationChannel(channel)

        return NotificationCompat.Builder(context, notificationData.channel.toString())
            .setSmallIcon(R.drawable.skillet_24)
            .setAutoCancel(true)
            .setContentText(context.getString(notificationData.description))
            .setContentTitle(context.getString(notificationData.title))
            .setPriority(PRIORITY_MAX)
            .setCategory(CATEGORY_ALARM)
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
            .build()
    }

    private fun createChannel(channel: NotificationData.Channel): NotificationChannel =
        NotificationChannel(
            channel.toString(),
            context.getString(channel.displayNameRes),
            channel.importance
        )
}