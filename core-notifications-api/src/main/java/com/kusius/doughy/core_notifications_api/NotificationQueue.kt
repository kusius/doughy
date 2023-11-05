package com.kusius.doughy.core.notifications.api

interface NotificationQueue {
    suspend fun add(notification: NotificationData)

    suspend fun remove(notification: NotificationData)

    suspend fun size(): Int

    suspend fun clear()

    suspend fun peek(): NotificationData?

}