package com.kusius.doughy.core.model

data class Recipe(
    val uid: Int,
    val name: String,
    val percents: Percents,
    val rests: Rests,
    val description: String,
    val isCustom: Boolean
)
