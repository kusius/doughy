package com.kusius.doughy.feature.recipe.ui

import com.kusius.doughy.core.model.Percents
import com.kusius.doughy.core.model.Recipe
import com.kusius.doughy.core.model.Rests
import com.kusius.doughy.core.model.YeastType
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateGramsUserCaseTest {

    val testRecipe = Recipe(
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
    fun grams_are_correct() {
        val useCase = CalculateGramsUseCase(testRecipe.percents)
        val grams = useCase(881)
        println(grams)

        assertEquals(185, grams.pFlour)
        assertEquals(185, grams.pWater)
        assertEquals(3.0f, grams.pSugars,  0.1f)
        assertEquals(6.0f, grams.pYeast,  0.1f)
        assertEquals(696, grams.flour)
        assertEquals(405, grams.water)
        assertEquals(23.8f, grams.salt, 0.1f)
    }
}