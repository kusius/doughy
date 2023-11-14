package com.kusius.doughy.feature.recipe.ui

import com.kusius.doughy.core.model.Percents
import com.kusius.doughy.core.model.Recipe
import com.kusius.doughy.core.model.Rests
import com.kusius.doughy.core.model.YeastType
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.datetime.Instant
import org.junit.Test

class CalculateScheduleUseCaseTest {
    val testRecipe =
        Recipe(
            name = "Poolish Dough",
            percents = Percents(
                hydrationPercent = 0.65f,
                oilPercent = 0.0f,
                saltPercent = 0.027f,
                sugarsPercent = 0.0034f,
                yeastPercent = 0.0068f,
                yeastType = YeastType.FRESH,
                prefermentPercent = 0.21f,
                prefermentHydrationPercent = 1f,
                prefermentUsesYeast = true,
            ),
            rests = Rests(
                prefermentRestHours = 16,
                bulkRestHours = 16,
                ballsRestHours = 6
            ),
            description = "A simple poolish recipe"
        )

    @Test
    fun cook_time_is_correct() {
        val useCase = CalculateScheduleUseCase(testRecipe)
        val targetInstant = Instant.parse("2023-11-16T20:54:00.000Z")

        val result = useCase(targetInstant.toEpochMilliseconds())

        val lastStep = result.lastOrNull()

        assertNotNull(lastStep)
        assertEquals(targetInstant, Instant.fromEpochMilliseconds(lastStep!!.time))

    }
}