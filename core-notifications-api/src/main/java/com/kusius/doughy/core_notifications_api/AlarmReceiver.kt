package com.kusius.doughy.core_notifications_api

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi

private const val ACTION_START_ALARM_LOOPER = "alarm.loopers.START"

fun Context.startAlarmLooper(loopReceiverClass: Class<out AlarmReceiver>) { // <1>
    sendBroadcast(Intent(this, loopReceiverClass).apply {
        action = ACTION_START_ALARM_LOOPER
    })
}

abstract class AlarmReceiver : BroadcastReceiver() {
    abstract val alarmManager: AlarmManager

    @CallSuper // <2>
    override fun onReceive(context: Context, intent: Intent) {
        val isStartAction =
            intent.action == ACTION_START_ALARM_LOOPER || intent.action == Intent.ACTION_BOOT_COMPLETED // <3>

        if (isStartAction) {
            // get the notifications stored in the queue and resubmit them
        }
    }

    protected fun stop(context: Context) { // remove all alarms from queue

    }

//    @SuppressLint("MissingPermission") // provided in :common:notifications
//    private fun vintageLoopInternal(intent: PendingIntent) { //
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            System.currentTimeMillis() + 1000L,
//            intent
//        )
//    }
//
//    @RequiresApi(Build.VERSION_CODES.S)
//    private fun api31loopInternal(intent: PendingIntent) { // <8>
//        if (alarmManager.canScheduleExactAlarms()) {
//            vintageLoopInternal(intent)
//        } else {
//            alarmManager.setAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                System.currentTimeMillis() + 1000L,
//                intent
//            )
//        }
//    }

//    private fun getLoopIntent(context: Context, noCreate: Boolean): PendingIntent? = // <10>
//        PendingIntent.getBroadcast(
//            context,
//            0,
//            Intent(context, this::class.java).apply { action = loopAction },
//            if (noCreate) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE else PendingIntent.FLAG_IMMUTABLE
//        )
}