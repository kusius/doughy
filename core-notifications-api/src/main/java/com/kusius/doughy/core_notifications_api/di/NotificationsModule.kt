package com.kusius.doughy.core_notifications_api.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kusius.doughy.core.notifications.api.AlarmReceiver
import com.kusius.doughy.core.notifications.api.NotificationQueue
import com.kusius.doughy.core.notifications.api.PersistentNotificationQueue
import com.kusius.doughy.core_notifications_api.Api26SystemNotificationBuilder
import com.kusius.doughy.core_notifications_api.SystemNotificationBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NotificationsModule {
    @Singleton
    @Provides
    fun bindsNotificationQueue(dataStore: DataStore<Preferences>): NotificationQueue =
        PersistentNotificationQueue(dataStore)

    @Provides
    fun bindsAlarmManager(@ApplicationContext appContext: Context): AlarmManager {
        return appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    fun bindsNotificationManager(@ApplicationContext appContext: Context): NotificationManager {
        return appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    fun bindsNotificationBuilder(
        @ApplicationContext appContext: Context,
        notificationManager: NotificationManager
    ): SystemNotificationBuilder {
        return Api26SystemNotificationBuilder(appContext, notificationManager)
    }

    @Singleton
    @Provides
    fun bindsAlarmReceiver(notificationQueue: NotificationQueue): AlarmReceiver {
        return AlarmReceiver()
    }
}