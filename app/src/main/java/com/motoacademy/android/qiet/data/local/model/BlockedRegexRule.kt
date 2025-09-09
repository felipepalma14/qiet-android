package com.motoacademy.android.qiet.data.local.model
import kotlinx.serialization.Serializable


@Serializable
data class BlockedRegexRule(
    val pattern: String
)
