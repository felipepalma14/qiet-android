package com.motoacademy.android.qiet.data.local.model
import kotlinx.serialization.Serializable
@Serializable
data class BlockedContact(
    val phoneNumber: String,
    val displayName: String?
)