package com.kusius.doughy.core.model

data class Percents(
    val hydrationPercent: Float,
    val oilPercent: Float,
    val saltPercent: Float,
    val sugarsPercent: Float,
    val yeastPercent: Float,
    val yeastType: YeastType,
    val prefermentPercent: Float,
    val prefermentHydrationPercent: Float,
    val prefermentUsesYeast: Boolean,
)

data class Grams(
    // preferment
    val pFlour: Int,
    val pWater: Int,
    val pYeast: Float,
    val pSugars: Float,

    // dough
    val flour: Int,
    val water: Int,
    val oil: Float,
    val salt: Float,
)