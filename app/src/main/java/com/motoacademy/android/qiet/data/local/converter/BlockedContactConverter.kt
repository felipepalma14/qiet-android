package com.motoacademy.android.qiet.data.local.converter

import com.motoacademy.android.qiet.data.local.model.BlockedContact
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BlockedContactConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromBlockedContactListToJson(value: List<BlockedContact>?): String? {
        return value?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun fromJsonToBlockedContactList(value: String?): List<BlockedContact> {
        return value
            ?.takeIf { it.isNotBlank() }
            ?.let {
                try {
                    json.decodeFromString<List<BlockedContact>>(it)
                } catch (e: Exception) {
                    emptyList()
                }
            } ?: emptyList()
    }
}
