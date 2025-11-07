package com.motoacademy.android.qiet.features.call_history.model

data class BlockedCallSpamUi(
    val id: Long,
    val name: String,
    val number: String,
    val reason: String,
    val createdAt: Long,
    val formattedDate: String
)