package com.kusius.doughy.core.notifications.api

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.CallSuper
import androidx.multidex.BuildConfig
import com.kusius.doughy.core_notifications_api.SystemNotificationBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

private const val ACTION_START_ALARM_LOOPER = "alarm.loopers.START"
private const val ACTION_LOOPER_LOOP = "alarm.loopers.LOOP"

fun Context.startAlarmLooper(loopReceiverClass: Class<out AlarmReceiver>) { // <1>
    // enable the receiver in case it is not
    val receiver = ComponentName(this, loopReceiverClass)
    packageManager.setComponentEnabledSetting(
        receiver,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )

    // trigger the start of the receiver loop
    sendBroadcast(Intent(this, loopReceiverClass).apply {
        action = ACTION_START_ALARM_LOOPER
    })
}

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "AlarmReceiver"
    }

    @Inject
    lateinit var queue: NotificationQueue

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder: SystemNotificationBuilder

    private val loopPeriod = 15000L

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "onReceive: action : ${intent.action}")

        runBlocking(Dispatchers.IO) {
            val nextNotification = queue.peek()
            val wakeUpAtTime = if (nextNotification != null) {
                if (Clock.System.now().toEpochMilliseconds() >= nextNotification.time) {
                    Log.i(TAG, "onReceive: showing notification $nextNotification ")
                    showNotification(nextNotification)
                    queue.remove(nextNotification)
                    Clock.System.now().toEpochMilliseconds() + loopPeriod
                } else {
                    nextNotification.time
                }
            } else {
                Clock.System.now().toEpochMilliseconds() + loopPeriod
            }
            wakeUpAt(wakeUpAtTime, context)
        }
    }

    fun stop(context: Context) { // remove all alarms from queue

    }

    private fun showNotification(notificationData: NotificationData) {
        val notification = notificationBuilder.buildSystemNotification(notificationData)
        notificationManager.notify(notificationData.id, notification)
    }

    private fun wakeUpAt(triggerAtMillis: Long, context: Context) {
        Log.i(
            TAG, "wakeUpAt: ${
                Instant.fromEpochMilliseconds(triggerAtMillis).toLocalDateTime(
                    TimeZone.currentSystemDefault()
                )
            }"
        )
        val canScheduleExactAlarms =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
                alarmManager.canScheduleExactAlarms() else true

        if (canScheduleExactAlarms) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                getWakeUpIntent(context, false)!!
            )
        }
    }

    private fun getWakeUpIntent(context: Context, noCreate: Boolean): PendingIntent? {
        return PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, this::class.java).apply { action = ACTION_LOOPER_LOOP },
            if (noCreate) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE else PendingIntent.FLAG_IMMUTABLE
        )
    }

}