package com.kusius.doughy.core.model

data class Schedule(
    val type: Type,
    val time: Long
)

enum class Type {
    PREFERMENT,
    BULK,
    BALLS,
    PREHEAT,
    COOK
}