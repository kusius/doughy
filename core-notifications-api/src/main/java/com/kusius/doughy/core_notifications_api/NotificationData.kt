package com.kusius.doughy.core_notifications_api

import android.app.NotificationManager
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable

@Serializable
data class NotificationData(
    val id: Int,
    val channel: Channel, // <1>
    val title: String,
    val description: String,
    val icon: Icon, // <3>
    val action: String? = null // <5>
)


@Serializable
sealed class Icon : java.io.Serializable {

    @Serializable
    data class Res(@DrawableRes val resId: Int) : Icon()

    @Serializable
    data class Url(val url: String) : Icon()
}

enum class Channel(
    @StringRes val displayNameRes: Int,
    val importance: Int
) {
    SCHEDULED(
        R.string.notifications_scheduled_channel_display_name,
        NotificationManager.IMPORTANCE_HIGH
    ),
}
