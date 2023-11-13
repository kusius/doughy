package com.kusius.doughy.feature.recipe.ui

import com.kusius.doughy.core.model.Recipe
import com.kusius.doughy.core.model.Schedule
import com.kusius.doughy.core.model.Type
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.minus

class CalculateScheduleUseCase(private val recipe: Recipe) {
    operator fun invoke(targetTime: Long): List<Schedule> {
        val targetInstant = Instant.fromEpochMilliseconds(targetTime)
        val steps = mutableListOf<Schedule>()

        // cook
        steps.add(
            Schedule(
                type = Type.COOK,
                time = targetInstant.toEpochMilliseconds()
            )
        )

        // oven preparation and mise en place
        val preheatOvenInstant = targetInstant.minus(1, DateTimeUnit.HOUR)
        steps.add(
            Schedule(
                type = Type.PREHEAT,
                time = preheatOvenInstant.toEpochMilliseconds()
            )
        )

        // dough balls preparation
        val doughBallsInstant = preheatOvenInstant.minus(recipe.rests.ballsRestHours, DateTimeUnit.HOUR)
        steps.add(
            Schedule(
                type = Type.BALLS,
                time = doughBallsInstant.toEpochMilliseconds()
            )
        )

        // bulk dough preparation
        val bulkInstant = doughBallsInstant.minus(recipe.rests.bulkRestHours, DateTimeUnit.HOUR)
        steps.add(
            Schedule(
                type = Type.BULK,
                time = bulkInstant.toEpochMilliseconds()
            )
        )


        // preferment preparation
        val prefermentInstant = bulkInstant.minus(recipe.rests.prefermentRestHours, DateTimeUnit.HOUR)
        steps.add(
            Schedule(
                type = Type.PREFERMENT,
                time = prefermentInstant.toEpochMilliseconds()
            )
        )

        steps.reverse()
        return steps.toList()
    }
}