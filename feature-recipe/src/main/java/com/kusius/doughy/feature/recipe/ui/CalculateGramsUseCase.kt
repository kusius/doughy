package com.kusius.doughy.feature.recipe.ui

import com.kusius.doughy.core.model.Grams
import com.kusius.doughy.core.model.Percents
import kotlin.math.roundToInt

fun Float.formatForDisplay(): String {
    return "%.1f".format(this)
}

class CalculateGramsUseCase(private val percents: Percents) {
    operator fun invoke(totalFlourGrams: Int): Grams {
        val prefermentFlour = (totalFlourGrams * percents.prefermentPercent).roundToInt()
        val prefermentWater = (totalFlourGrams * this.percents.prefermentPercent * this.percents.prefermentHydrationPercent).roundToInt()
        val salt = (totalFlourGrams * this.percents.saltPercent)
        val sugars = (totalFlourGrams * this.percents.sugarsPercent)
        return Grams(
            pFlour = prefermentFlour,
            pWater = prefermentWater,
            pSugars = sugars,
            pYeast = (totalFlourGrams * this.percents.yeastPercent),
            flour = (totalFlourGrams - prefermentFlour),
            water = (this.percents.hydrationPercent * (totalFlourGrams + salt + sugars) - prefermentWater).roundToInt(),
            oil = (totalFlourGrams * this.percents.oilPercent),
            salt = salt
        )
    }
}