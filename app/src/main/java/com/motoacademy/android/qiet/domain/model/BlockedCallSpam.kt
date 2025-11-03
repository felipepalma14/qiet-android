package com.motoacademy.android.qiet.domain.model

data class BlockedCallSpam(
    val id: Long,
    val name: String,
    val number: String,
    val reason: String, // block rule
    val createdAt: Long
)
