package com.motoacademy.android.qiet.data.local.converter

import com.motoacademy.android.qiet.data.local.model.BlockedContact
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class BlockedContactConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromList(value: List<BlockedContact>?): String {
        return value?.let { json.encodeToString(it) } ?: "[]"
    }

    @TypeConverter
    fun toList(value: String): List<BlockedContact> {
        return json.decodeFromString(value)
    }
}
