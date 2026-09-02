package com.kusius.doughy.core.model

/**
 * The recipes every install starts with. This is the single definition: the database seeds itself
 * from these and previews render them, so the two can no longer drift apart.
 *
 * [Recipe.uid] is a placeholder here; the database assigns the real one when a recipe is inserted.
 */
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
    uid = 0,
)

val predefinedRecipes = listOf(samplePoolishRecipe, sampleBigaRecipe)
