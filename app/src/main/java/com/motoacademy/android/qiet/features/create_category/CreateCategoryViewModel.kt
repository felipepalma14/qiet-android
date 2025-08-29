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
    private val _timeRestrictionsEnabled = MutableStateFlow(false)
    private val _startTime = MutableStateFlow("08:00")
    private val _endTime = MutableStateFlow("22:00")
    private val _weekDays = MutableStateFlow<Set<String>>(emptySet())

    private val _isSaving = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)


//    val uiState: StateFlow<CreateCategoryUiState> = combine(
//        _categoryName, _categoryColorHex, _blockedPrefixes, _blockedContacts,
//        _timeRestrictionsEnabled, _startTime, _endTime, _weekDays, _isSaving, _error
//    ) { name, color, prefixes, contacts, restrictionsEnabled, start, end, days, saving, err ->
//        CreateCategoryUiState(
//            categoryName = name,
//            categoryColorHex = color,
//            blockedPrefixes = prefixes,
//            blockedContacts = contacts,
//            timeRestrictionsEnabled = restrictionsEnabled,
//            startTime = start,
//            endTime = end,
//            weekDays = days,
//            isSaving = saving,
//            error = err
//        )
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = CreateCategoryUiState()
//    )


}