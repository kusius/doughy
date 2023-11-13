package com.kusius.doughy.core.notifications.api

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class NotificationDataSerializationTest {

    private val testNotification = NotificationData(
        id = 1,
        channel = NotificationData.Channel.SCHEDULED,
        title = "Notification",
        description = "This is a notification",
        icon = NotificationData.Icon.Url(url ="www.google.com"),
        action = null
    )

@Test
    fun notification_serialized_correctly() {
        val json = Json.encodeToString(testNotification)
        val reconstructed: NotificationData = Json.decodeFromString(json)
        assertEquals(expected = testNotification, actual = reconstructed)
    }
}