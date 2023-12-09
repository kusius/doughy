package com.kusius.doughy.core.database.migrations

import com.kusius.doughy.core.database.RecipeEntity
import com.kusius.doughy.core.model.YeastType


internal val samplePoolishRecipe = RecipeEntity(
    name = "Poolish Dough",

    hydrationPercent = 0.65f,
    oilPercent = 0.0f,
    saltPercent = 0.027f,
    sugarsPercent = 0.0034f,
    yeastPercent = 0.0068f,
    yeastType = YeastType.FRESH,
    prefermentPercent = 0.21f,
    prefermentHydrationPercent = 1f,
    prefermentUsesYeast = true,
    prefermentRestHours = 16,
    bulkRestHours = 16,
    ballsRestHours = 3,

    description = "A simple poolish recipe",
    isCustom = false
)

internal val sampleBigaRecipe = RecipeEntity(
    name = "Biga dough",

    hydrationPercent = 0.75f,
    oilPercent = 0.0f,
    saltPercent = 0.03f,
    sugarsPercent = 0.00f,
    yeastPercent = 0.003f,
    yeastType = YeastType.FRESH,
    prefermentPercent = 0.5f,
    prefermentHydrationPercent = 0.5f,
    prefermentUsesYeast = true,

    prefermentRestHours = 48,
    bulkRestHours = 0,
    ballsRestHours = 2,
    description = "Results in an elastic dough, with a dry alcoholic preferment. A mixer is essential for this recipe!",
    isCustom = false
)

internal val predefinedRecipes = listOf(samplePoolishRecipe, sampleBigaRecipe)