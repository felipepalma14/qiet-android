package com.motoacademy.android.qiet.domain.model

import com.motoacademy.android.qiet.data.local.model.BlockedContact
import com.motoacademy.android.qiet.data.local.model.IntervalTime

data class BlockRule(

    val id: Long = 0L,

    val ruleName: String,
    val isEnabled: Boolean,
    val color: String,

    val blockedContacts: List<BlockedContact> = emptyList(),
    val blockedRegexRules: List<String> = emptyList(),

    val interval: IntervalTime? = null,
)
