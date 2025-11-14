package com.motoacademy.android.qiet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

import com.motoacademy.android.qiet.data.local.converter.StringListConverter
import com.motoacademy.android.qiet.data.local.converter.BlockedContactConverter
import com.motoacademy.android.qiet.data.local.converter.IntervalTimeConverter

import com.motoacademy.android.qiet.data.local.model.BlockedContact
import com.motoacademy.android.qiet.data.local.model.IntervalTime

@Entity (tableName = "block_rule")
@TypeConverters(
    BlockedContactConverter::class,
     StringListConverter::class,
    IntervalTimeConverter::class
)

data class BlockRuleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val ruleName: String,
    val isEnabled: Boolean,
    val color: String? = "#FFFFFF",

    val blockedContacts: List<BlockedContact> = emptyList(),
    val blockedRegexRules: List<String> = emptyList(),

    val interval: IntervalTime? = null,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()

)