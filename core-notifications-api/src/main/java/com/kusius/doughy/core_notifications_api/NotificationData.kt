package com.kusius.doughy.core.notifications.api

import android.app.NotificationManager
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable

@Serializable
data class NotificationData(
    val id: Int,
    val channel: Channel, // <1>
    // Resolved text rather than string resource ids: a queued notification outlives the build
    // it was created by, and resource ids are regenerated on every build.
    val title: String,
    val description: String,
    val action: String? = null, // <5>
    val time: Long
) {

    enum class Channel(
        @StringRes val displayNameRes: Int,
        val importance: Int
    ) {
        SCHEDULED(
            R.string.notifications_scheduled_channel_display_name,
            NotificationManager.IMPORTANCE_HIGH
        ),
    }
}
