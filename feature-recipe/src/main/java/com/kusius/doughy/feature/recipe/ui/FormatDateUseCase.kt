package com.kusius.doughy.feature.recipe.ui

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.Locale

class FormatDateUseCase() {
    operator fun invoke(timeMillis: Long): String {
        val time = Instant.fromEpochMilliseconds(timeMillis).toLocalDateTime(TimeZone.currentSystemDefault())
        return "${time.dayOfMonth} ${time.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} ${time.hour} : ${time.minute}"
    }
}