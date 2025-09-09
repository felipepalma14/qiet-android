package com.motoacademy.android.qiet.data.local.model

data class IntervalEmbeddable(

    val startTime: String,
    val endTime: String,
    val daysOfWeek: String //isso poderia ser uma sealed class de dias da semana?
)