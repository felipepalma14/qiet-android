package com.motoacademy.android.qiet.domain.model

import com.motoacademy.android.qiet.data.local.model.BlockedContact
import com.motoacademy.android.qiet.data.local.model.IntervalTime

data class BlockRule(
    val id: Long = 0,
    val ruleName: String,
    val isEnabled: Boolean,
    val color: String? = null,
    val blockedContacts: List<BlockedContact>,
    val blockedRegexRules: List<String>,
    val interval: IntervalTime?,
    val createdAt: Long,
    val updatedAt: Long
)

