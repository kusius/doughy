package com.kusius.doughy.core_notifications_api

interface NotificationQueue {
    suspend fun add(notification: NotificationData)

    suspend fun remove(notification: NotificationData)
}