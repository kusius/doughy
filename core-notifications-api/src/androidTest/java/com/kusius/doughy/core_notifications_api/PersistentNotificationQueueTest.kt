package com.kusius.doughy.core_notifications_api

import android.content.Context
import com.kusius.doughy.core.notifications.api.AlarmReceiver
import com.kusius.doughy.core.notifications.api.NotificationData
import com.kusius.doughy.core.notifications.api.PersistentNotificationQueue
import com.kusius.doughy.core.notifications.api.startAlarmLooper
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class PersistentNotificationQueueTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var queue: PersistentNotificationQueue

    @Inject
    lateinit var alarmReceiver: AlarmReceiver

    @ApplicationContext
    lateinit var appContext: Context

    @Before
    fun init() {
        hiltRule.inject()
    }

    private val testNotification = NotificationData(
        id = 1,
        channel = NotificationData.Channel.SCHEDULED,
        title = 44,
        description = 44,
        icon = NotificationData.Icon.Url(url ="www.google.com"),
        action = null,
        time = System.currentTimeMillis() + 1000L
    )

    @Test
    fun queue_keys_work() = runTest {
        queue.clear()
        assertEquals(0, queue.size())
        queue.add(testNotification)
        assertEquals(1, queue.size())
        queue.remove(testNotification)
        assertEquals(0, queue.size())
    }
}