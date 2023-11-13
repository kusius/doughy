package com.kusius.doughy.core.model

data class Rests(
    val prefermentRestHours: Int,
    val bulkRestHours: Int,
    val ballsRestHours: Int,
) {
    fun totalRestHours() = prefermentRestHours + bulkRestHours + ballsRestHours
}