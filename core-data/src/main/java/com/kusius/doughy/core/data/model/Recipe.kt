package com.kusius.doughy.core.data.model

import com.kusius.doughy.core.database.RecipeEntity
import com.kusius.doughy.core.model.Recipe

internal fun Recipe.asEntity() = RecipeEntity(
    name = name,
    prefermentPercent = percents.prefermentPercent,
    hydrationPercent = percents.hydrationPercent,
    oilPercent = percents.oilPercent,
    saltPercent = percents.saltPercent,
    sugarsPercent = percents.sugarsPercent,
    yeastPercent = percents.yeastPercent,
    yeastType = percents.yeastType,
    prefermentHydrationPercent = percents.prefermentHydrationPercent,
    prefermentUsesYeast = percents.prefermentUsesYeast,
    prefermentRestHours = rests.prefermentRestHours,
    bulkRestHours = rests.bulkRestHours,
    ballsRestHours = rests.ballsRestHours,
    description = description,
    isCustom = isCustom,
)