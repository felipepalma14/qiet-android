package com.motoacademy.android.qiet.features.create_category

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import androidx.lifecycle.viewModelScope

@HiltViewModel
class CreateCategoryViewModel @Inject constructor() : ViewModel() {

    private val _categoryName = MutableStateFlow("")
    private val _categoryColorHex = MutableStateFlow("#FFFFFFFF")
    private val _blockedPrefixes = MutableStateFlow<List<String>>(emptyList())
    private val _blockedContacts = MutableStateFlow<List<String>>(emptyList())

    private val categoryData: StateFlow<CategoryDataState> = combine(
        _categoryName, _categoryColorHex, _blockedPrefixes, _blockedContacts
    ) { name, color, prefixes, contacts ->
        CategoryDataState(
            name,
            color,
            prefixes,
            contacts)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CategoryDataState()
    )

    private val _timeRestrictionsEnabled = MutableStateFlow(false)
    private val _startTime = MutableStateFlow("08:00")
    private val _endTime = MutableStateFlow("22:00")
    private val _weekDays = MutableStateFlow<Set<String>>(emptySet())

    private val timeRestrictions: StateFlow<TimeRestrictionsState> = combine(
        _timeRestrictionsEnabled, _startTime, _endTime, _weekDays
    ) { enabled, start, end, days ->
        TimeRestrictionsState(
            enabled,
            start,
            end,
            days)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TimeRestrictionsState()
    )


    private val _isSaving = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val feedback: StateFlow<UiFeedbackState> = combine(
        _isSaving, _error
    ) { saving, err ->
        UiFeedbackState(saving, err)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UiFeedbackState()
    )


    val uiState: StateFlow<CreateCategoryUiState> = combine(
        categoryData, timeRestrictions, feedback
    ) { category, time, fb ->
        CreateCategoryUiState(
            category,
            time,
            fb)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CreateCategoryUiState()
    )


}