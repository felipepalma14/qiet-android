package com.motoacademy.android.qiet.features.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motoacademy.android.qiet.data.local.model.DayOfWeek
import com.motoacademy.android.qiet.data.mapper.toModel
import com.motoacademy.android.qiet.domain.usecase.GetAllBlockRulesUseCase
import com.motoacademy.android.qiet.domain.usecase.UpdateRuleEnabledUseCase
import com.motoacademy.android.qiet.ui.components.list.BlockRule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockDashboardViewModel @Inject constructor(
    val getAllBlockRulesUseCase: GetAllBlockRulesUseCase,
    val updateRuleEnabledUseCase: UpdateRuleEnabledUseCase
): ViewModel() {

    // Expose immutable StateFlow to the UI
    private val _rules = MutableStateFlow<List<BlockRule>>(emptyList())
    val rules: StateFlow<List<BlockRule>> = _rules.asStateFlow()

    init {
        observeRules()
    }

    private fun observeRules() {
        viewModelScope.launch {
            getAllBlockRulesUseCase().collect { ruleList ->
                _rules.value = ruleList.map {
                   val model = it.toModel()
                    BlockRule(
                        id = model.id,
                        title = model.ruleName,
                        prefixLabel = if (model.blockedRegexRules.isNotEmpty()) "Prefixo(s): ${model.blockedRegexRules.joinToString(", ")}" else null,
                        blockInterval = model.interval?.let { interval -> "Bloqueio: ${interval.startTime} - ${interval.endTime}" } ,
                        blockWeek = model.interval?.daysOfWeek?.let { days -> formatSelectedDays(days) },
                        isChecked = model.isEnabled,
                        blockedContactLabel = if (model.blockedContacts.isNotEmpty()) "${model.blockedContacts.size} Contato(s) bloqueado(s)" else null
                    )

                }
            }
        }
    }

    fun onRuleEnabledChange(ruleId: Long, isEnabled: Boolean) {
        viewModelScope.launch {
            updateRuleEnabledUseCase(ruleId, isEnabled)
        }
    }

    private fun formatSelectedDays(days: List<DayOfWeek>): String {
        if (days.isEmpty()) return "No days selected"

        val sortedDays = days.sortedBy { it.ordinal }

        val allDays = DayOfWeek.entries
        val weekend = listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        val weekdays = listOf(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
        )

        return when {
            sortedDays.containsAll(weekend) && sortedDays.size == 2 -> "Final de semana"
            sortedDays.containsAll(allDays) -> "Todos os dias"
            sortedDays.containsAll(weekdays) && sortedDays.size == 5 -> "Dias de semana"
            else -> sortedDays.joinToString(", ") { it.name.substring(0,3).lowercase().replaceFirstChar(Char::titlecase) }
        }
    }

}
