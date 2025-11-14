package com.motoacademy.android.qiet.data.local.model

import kotlinx.serialization.Serializable

@Serializable
enum class DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

@Serializable
data class IntervalTime(
    val startTime: String,
    val endTime: String,
    val daysOfWeek:  List<DayOfWeek>
)

fun DayOfWeek.toPtBrLabel(): String = when (this) {
    DayOfWeek.MONDAY -> "Seg"
    DayOfWeek.TUESDAY -> "Ter"
    DayOfWeek.WEDNESDAY -> "Qua"
    DayOfWeek.THURSDAY -> "Qui"
    DayOfWeek.FRIDAY -> "Sex"
    DayOfWeek.SATURDAY -> "Sáb"
    DayOfWeek.SUNDAY -> "Dom"
}