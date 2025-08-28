package com.motoacademy.android.qiet.features.create_category

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

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

    // Estados auxiliares para a UI
    private val _isSaving = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

}