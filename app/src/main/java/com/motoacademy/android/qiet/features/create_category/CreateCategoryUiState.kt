package com.motoacademy.android.qiet.features.create_category

data class CreateCategoryUiState (
    val categoryName: String = "",
    val categoryColorHex: String = "#FFFFFF",
    val blockedPrefixes: List<String> = emptyList(),
    val blockedContacts: List<String> = emptyList(),
    val timeRestrictionsEnabled: Boolean = false,
    val startTime: String = "08:00",
    val endTime: String = "22:00",
    val weekDays: Set<String> = emptySet(),
    val isSaving: Boolean = false,
    val error: String? = null

)