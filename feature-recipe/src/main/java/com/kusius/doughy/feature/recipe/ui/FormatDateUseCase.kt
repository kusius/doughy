package com.kusius.doughy.feature.recipe.ui

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.Locale

class FormatDateUseCase() {
    operator fun invoke(timeMillis: Long): String {
        val time = Instant.fromEpochMilliseconds(timeMillis).toLocalDateTime(TimeZone.currentSystemDefault())
        val month = time.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val hour = time.hour.toString().padStart(2, '0')
        val minute = time.minute.toString().padStart(2, '0')
        return "${time.dayOfMonth} $month $hour:$minute"
    }
}