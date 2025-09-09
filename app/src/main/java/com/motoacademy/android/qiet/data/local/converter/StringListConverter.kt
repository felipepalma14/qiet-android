package com.motoacademy.android.qiet.data.local.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class StringListConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromList(value: List<String>?): String {
        return value?.let { json.encodeToString(it) } ?: "[]"
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return json.decodeFromString(value)
    }
}

