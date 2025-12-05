package com.motoacademy.android.qiet.features.create_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motoacademy.android.qiet.data.local.model.DayOfWeek
import com.motoacademy.android.qiet.data.local.model.IntervalTime
import com.motoacademy.android.qiet.domain.model.BlockRule
import com.motoacademy.android.qiet.domain.usecase.AddBlockRuleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- STATE ---
data class RuleState(
    val ruleName: String = "",
    val prefixList: List<String> = emptyList(),
    val isEnabled: Boolean = false,
    //val color: Color = BlueCategory,
    val timeRestrictionEnabled: Boolean = false,
    val startTime: String = "09:00",
    val endTime: String = "22:00",
    val selectedDays: List<DayOfWeek> = emptyList(),
    val prefixInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isFormValid: Boolean = false,
)

sealed interface RuleEvent {
    data class OnRuleNameChanged(val value: String) : RuleEvent
    data class OnToggleEnabled(val value: Boolean) : RuleEvent
    //data class OnColorChanged(val value: Color) : AddRuleEvent

    data class OnAddPrefix(val prefix: String) : RuleEvent
    data class OnRemovePrefix(val prefix: String) : RuleEvent

    data class OnToggleTimeRestriction(val value: Boolean) : RuleEvent
    data class OnStartTimeChanged(val value: String) : RuleEvent
    data class OnEndTimeChanged(val value: String) : RuleEvent
    data class OnDaySelected(val day: DayOfWeek) : RuleEvent
    data class OnPrefixInputChanged(val value: String) : RuleEvent

    object OnCreateClicked : RuleEvent
    object OnCancelClicked : RuleEvent
}

// EFFECT (one time)
sealed interface RuleEffect {
    object NavigateBack : RuleEffect
    object Success : RuleEffect
    data class ShowError(val message: String) : RuleEffect
}

@HiltViewModel
class CreateCategoryViewModel @Inject constructor(
    private val saveBlockRuleUseCase: AddBlockRuleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RuleState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RuleEffect>()
    val effect = _effect.asSharedFlow()


    fun onEvent(event: RuleEvent) {
        when (event) {
            is RuleEvent.OnRuleNameChanged -> update { it.copy(ruleName = event.value) }

            is RuleEvent.OnToggleEnabled -> update { it.copy(isEnabled = event.value) }

            //is RuleEvent.OnColorChanged -> update { it.copy(color = event.value) }

            is RuleEvent.OnRemovePrefix -> update {
                it.copy(prefixList = it.prefixList - event.prefix)
            }

            is RuleEvent.OnToggleTimeRestriction -> update {
                it.copy(timeRestrictionEnabled = event.value)
            }

            is RuleEvent.OnStartTimeChanged -> update {
                it.copy(startTime = event.value)
            }

            is RuleEvent.OnEndTimeChanged -> update {
                it.copy(endTime = event.value)
            }

            is RuleEvent.OnDaySelected -> update {
                val updated = it.selectedDays.toMutableList()
                if (updated.contains(event.day)) updated.remove(event.day)
                else updated.add(event.day)

                it.copy(selectedDays = updated)
            }

            is RuleEvent.OnPrefixInputChanged -> update {
                 it.copy(prefixInput = event.value)
            }

            is RuleEvent.OnAddPrefix -> {
                if (event.prefix.isNotBlank()) {
                    update {
                        it.copy(
                            prefixList = it.prefixList + event.prefix,
                            prefixInput = ""
                        )
                    }
                }
            }

            RuleEvent.OnCancelClicked -> viewModelScope.launch {
                _effect.emit(RuleEffect.NavigateBack)
            }

            RuleEvent.OnCreateClicked -> {
                validateAndSubmit()
            }
        }

        validateForm()
    }


    private fun update(reducer: (RuleState) -> RuleState) {
        _state.value = reducer(_state.value)
    }

    private fun validateForm() {
        val s = _state.value

        val valid = s.ruleName.isNotBlank() &&
                s.prefixList.isNotEmpty() &&
                (!s.timeRestrictionEnabled ||
                                (s.startTime.isNotBlank() &&
                                        s.endTime.isNotBlank() &&
                                        s.selectedDays.isNotEmpty())
                        )

        update { it.copy(isFormValid = valid) }
    }

    private fun validateAndSubmit() {
        val s = _state.value

        when {
            s.ruleName.isBlank() -> emitError("O nome da regra é obrigatório.")
            s.prefixList.isEmpty() -> emitError("Adicione pelo menos um prefixo.")
            s.timeRestrictionEnabled && s.selectedDays.isEmpty() -> emitError("Selecione ao menos um dia.")
            s.timeRestrictionEnabled && s.startTime.isBlank() -> emitError("Hora inicial inválida.")
            s.timeRestrictionEnabled && s.endTime.isBlank() -> emitError("Hora final inválida.")

            else -> {
                viewModelScope.launch {
                    submitBlockRule()
                }
            }
        }
    }

    private suspend fun submitBlockRule() {
        val s = _state.value

        val rule = BlockRule(
            id = 0,
            ruleName = s.ruleName,
            isEnabled = s.isEnabled,
            //color = s.color.toString(), // OU hex definido por você
            blockedContacts = emptyList(), // não veio da UI – pode ajustar depois
            blockedRegexRules = s.prefixList,
            interval = if (s.timeRestrictionEnabled) {
                IntervalTime(
                    startTime = s.startTime,
                    endTime = s.endTime,
                    daysOfWeek = s.selectedDays
                )
            } else null,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        try {
            saveBlockRuleUseCase(rule)
            _effect.emit(RuleEffect.Success)

        } catch (e: Exception) {
            _effect.emit(RuleEffect.ShowError("Erro ao salvar: ${e.message}"))
        }
    }

    private fun emitError(msg: String) {
        viewModelScope.launch {
            _effect.emit(RuleEffect.ShowError(msg))
        }
    }

}