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

fun java.time.DayOfWeek.toAppDayOfWeek(): DayOfWeek =
    when (this) {
        java.time.DayOfWeek.MONDAY -> DayOfWeek.MONDAY
        java.time.DayOfWeek.TUESDAY -> DayOfWeek.TUESDAY
        java.time.DayOfWeek.WEDNESDAY -> DayOfWeek.WEDNESDAY
        java.time.DayOfWeek.THURSDAY -> DayOfWeek.THURSDAY
        java.time.DayOfWeek.FRIDAY -> DayOfWeek.FRIDAY
        java.time.DayOfWeek.SATURDAY -> DayOfWeek.SATURDAY
        java.time.DayOfWeek.SUNDAY -> DayOfWeek.SUNDAY
    }

fun DayOfWeek.toPtBrLabel(): String = when (this) {
    DayOfWeek.MONDAY -> "Seg"
    DayOfWeek.TUESDAY -> "Ter"
    DayOfWeek.WEDNESDAY -> "Qua"
    DayOfWeek.THURSDAY -> "Qui"
    DayOfWeek.FRIDAY -> "Sex"
    DayOfWeek.SATURDAY -> "Sáb"
    DayOfWeek.SUNDAY -> "Dom"
}