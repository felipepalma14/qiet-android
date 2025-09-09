package com.motoacademy.android.qiet.data.local.converter//package com.motoacademy.android.qiet.data.local.converter
//import androidx.room.TypeConverter
//class BlockedContactConverter {
//    @TypeConverter
//    fun fromList(value: List<BlockedContact>?): String {
//        return Gson().toJson(value)
//    }
//
//    @TypeConverter
//    fun toList(value: String): List<BlockedContact> {
//        val type = object : TypeToken<List<BlockedContact>>() {}.type
//        return Gson().fromJson(value, type)
//    }
//}