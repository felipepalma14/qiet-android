package com.motoacademy.android.qiet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.TypeConverters
import com.motoacademy.android.qiet.data.local.model.BlockedContact
import com.motoacademy.android.qiet.data.local.model.BlockedRegexRule
import com.motoacademy.android.qiet.data.local.model.IntervalEmbeddable
//import com.motoacademy.android.qiet.data.local.converter.BlockedRegexRuleConverter

@Entity (tableName = "block_rule")
//@TypeConverters(
//    BlockedContactConverter::class,
//    BlockedRegexRuleConverter::class,
//    DateTimeConverter::class
//)

data class BlockRuleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val ruleName: String,
    val isEnabled: Boolean,
    val color: String,

    val blockedContacts: List<BlockedContact> = emptyList(),
    val blockedRegexRules: List<String> = emptyList(),

    @Embedded
    val interval: IntervalEmbeddable? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()

)