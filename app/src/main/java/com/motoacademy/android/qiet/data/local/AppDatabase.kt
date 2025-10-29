package com.motoacademy.android.qiet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.motoacademy.android.qiet.data.local.converter.BlockedContactConverter
import com.motoacademy.android.qiet.data.local.converter.IntervalTimeConverter
import com.motoacademy.android.qiet.data.local.converter.StringListConverter
import com.motoacademy.android.qiet.data.local.dao.BlockRuleDao
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity

@Database(
    entities = [BlockRuleEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    BlockedContactConverter::class,
    StringListConverter::class,
    IntervalTimeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun blockRuleDao(): BlockRuleDao

}
