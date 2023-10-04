package com.kusius.doughy.core.model

data class Ingredients(
    val totalFlourGrams: Int,
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
