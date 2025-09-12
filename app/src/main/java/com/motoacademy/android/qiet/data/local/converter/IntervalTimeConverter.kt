package com.motoacademy.android.qiet.data.local.converter

import com.motoacademy.android.qiet.data.local.model.IntervalTime
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class IntervalTimeConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromIntervalToJson(value: IntervalTime?): String {
        return value?.let { json.encodeToString(it) } ?: "{}"
    }

    @TypeConverter
    fun fromJsonToStringList(value: String): IntervalTime? {
        return json.decodeFromString<IntervalTime>(value)
    }
}
