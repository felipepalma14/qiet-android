package com.motoacademy.android.qiet.data.local.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class StringListConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStringListToJson(value: List<String>?): String? {
        return value?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun fromJsonToStringList(value: String?): List<String> {
        return value
            ?.takeIf { it.isNotBlank() }
            ?.let {
                try {
                    json.decodeFromString<List<String>>(it)
                } catch (e: Exception) {
                    emptyList()
                }
            } ?: emptyList()
    }
}

