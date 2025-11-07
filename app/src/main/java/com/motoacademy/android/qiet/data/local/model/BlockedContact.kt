package com.motoacademy.android.qiet.data.local.model
import kotlinx.serialization.Serializable
@Serializable
data class BlockedContact(
    val displayName: String?,
    val phoneNumber: String,
)