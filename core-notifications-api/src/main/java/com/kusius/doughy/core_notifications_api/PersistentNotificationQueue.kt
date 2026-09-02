package com.kusius.doughy.core.notifications.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

private object PreferencesKeys {
    val NOTIFICATION_PREFERENCES_KEY = stringPreferencesKey("notications")
}

class PersistentNotificationQueue @Inject constructor (
    private val dataStore: DataStore<Preferences>,
): NotificationQueue {
    override suspend fun add(notification: NotificationData) {
        dataStore.editNotifications { it.toMutableList().apply { add(notification) } }
    }

    override suspend fun remove(notification: NotificationData) {
        dataStore.editNotifications { it.toMutableList().apply { remove(notification) } }
    }

    override suspend fun size(): Int {
        val oldNotifications = loadNotifications()
        return oldNotifications.size
    }

    private suspend fun DataStore<Preferences>.editNotifications(transform: (List<NotificationData>) -> List<NotificationData>) {
        val oldNotifications = loadNotifications()
        val newNotifications = transform(oldNotifications)

        edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATION_PREFERENCES_KEY] =
                Json.encodeToString(newNotifications)
        }
    }

    override suspend fun clear() {
        // Only the queue belongs to this class; the datastore is shared with the rest of the app.
        dataStore.edit { it.remove(PreferencesKeys.NOTIFICATION_PREFERENCES_KEY) }
    }

    override suspend fun peek(): NotificationData? {
        val notificationsList = loadNotifications()
        return if (notificationsList.isEmpty()) null else notificationsList.first()
    }

    suspend fun loadNotifications(): List<NotificationData> { // <4>
        val notificationsJson = dataStore.data.first()[PreferencesKeys.NOTIFICATION_PREFERENCES_KEY]
        return if (notificationsJson == null) emptyList()
        // A queue written by an older build may no longer decode; dropping it is better than
        // failing every read from then on.
        else runCatching { Json.decodeFromString<List<NotificationData>>(notificationsJson) }
            .getOrDefault(emptyList())
    }
}