package com.motoacademy.android.qiet.features.create_category

data class CategoryDataState(
    val categoryName: String = "",
    val categoryColorHex: String = "#FFFFFFFF",
    val blockedPrefixes: List<String> = emptyList(),
    val blockedContacts: List<String> = emptyList()
)

data class TimeRestrictionsState(
    val enabled: Boolean = false,
    val startTime: String = "08:00",
    val endTime: String = "22:00",
    val weekDays: Set<String> = emptySet()
)

data class UiFeedbackState(
    val isSaving: Boolean = false,
    val error: String? = null
)

data class CreateCategoryUiState(
    val categoryData: CategoryDataState = CategoryDataState(),
    val timeRestrictions: TimeRestrictionsState = TimeRestrictionsState(),
    val feedback: UiFeedbackState = UiFeedbackState()
)
