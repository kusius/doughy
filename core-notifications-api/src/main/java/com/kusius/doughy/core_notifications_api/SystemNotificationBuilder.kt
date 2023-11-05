package com.kusius.doughy.core_notifications_api

import android.app.Notification
import com.kusius.doughy.core.notifications.api.NotificationData

interface SystemNotificationBuilder {
    fun buildSystemNotification(notificationData: NotificationData): Notification
}