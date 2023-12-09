package com.kusius.doughy.core.data

import com.kusius.doughy.core.model.Percents
import com.kusius.doughy.core.model.Recipe
import com.kusius.doughy.core.model.Rests
import com.kusius.doughy.core.model.YeastType


val samplePoolishRecipe = Recipe(
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
        ballsRestHours = 3
    ),
    description = "A simple poolish recipe",
    isCustom = false,
    uid = 0,
)

val sampleBigaRecipe = Recipe(
    name = "Biga dough",
    percents = Percents(
        hydrationPercent = 0.75f,
        oilPercent = 0.0f,
        saltPercent = 0.03f,
        sugarsPercent = 0.00f,
        yeastPercent = 0.003f,
        yeastType = YeastType.FRESH,
        prefermentPercent = 0.5f,
        prefermentHydrationPercent = 0.5f,
        prefermentUsesYeast = true,
    ),
    rests = Rests(
        prefermentRestHours = 48,
        bulkRestHours = 0,
        ballsRestHours = 2
    ),
    description = "Results in an elastic dough, with a dry alcoholic preferment. A mixer is essential for this recipe!",
    isCustom = false,
    uid = 9
)

val predefinedRecipes = listOf<Recipe> (
    samplePoolishRecipe,
    sampleBigaRecipe
)