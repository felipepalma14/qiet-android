package com.motoacademy.android.qiet.features.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motoacademy.android.qiet.data.local.model.DayOfWeek
import com.motoacademy.android.qiet.data.mapper.toModel
import com.motoacademy.android.qiet.domain.usecase.DeleteBlockRuleUseCase
import com.motoacademy.android.qiet.domain.usecase.GetAllBlockRulesUseCase
import com.motoacademy.android.qiet.domain.usecase.UpdateBlockRuleUseCase
import com.motoacademy.android.qiet.domain.usecase.UpdateRuleEnabledUseCase
import com.motoacademy.android.qiet.ui.components.list.BlockRule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class BlockDashboardUiState {
    object Loading : BlockDashboardUiState()
    object Success : BlockDashboardUiState()
    data class Error(val message: String) : BlockDashboardUiState()
}

@HiltViewModel
class BlockDashboardViewModel @Inject constructor(
    private val getAllBlockRulesUseCase: GetAllBlockRulesUseCase,
    private val updateRuleEnabledUseCase: UpdateRuleEnabledUseCase,
    private val deleteBlockRuleUseCase: DeleteBlockRuleUseCase,
    private val updateBlockRuleUseCase: UpdateBlockRuleUseCase
) : ViewModel() {

    init {
        println("✅ [ViewModel] BlockDashboardViewModel inicializado")
    }


    private val _rules = MutableStateFlow<List<BlockRule>>(emptyList())
    val rules: StateFlow<List<BlockRule>> = _rules.asStateFlow()


    private val _uiState = MutableStateFlow<BlockDashboardUiState>(BlockDashboardUiState.Success)
    val uiState: StateFlow<BlockDashboardUiState> = _uiState.asStateFlow()

    init {
        observeRules()
    }

    private fun observeRules() {
        viewModelScope.launch {
            _uiState.value = BlockDashboardUiState.Loading
            try {
                getAllBlockRulesUseCase().collect { ruleList ->
                    _rules.value = ruleList.sortedByDescending { it.createdAt }.map {
                        val model = it.toModel()
                        BlockRule(
                            id = model.id,
                            title = model.ruleName,
                            prefixLabel = if (model.blockedRegexRules.isNotEmpty()) "Prefixo(s): ${model.blockedRegexRules.joinToString(", ")}" else null,
                            blockInterval = model.interval?.let { interval -> "Bloqueio: ${interval.startTime} - ${interval.endTime}" },
                            blockWeek = model.interval?.daysOfWeek?.let { days -> formatSelectedDays(days) },
                            isChecked = model.isEnabled,
                            blockedContactLabel = if (model.blockedContacts.isNotEmpty()) "${model.blockedContacts.size} Contato(s) bloqueado(s)" else null
                        )
                    }
                    _uiState.value = BlockDashboardUiState.Success
                }
            } catch (e: Exception) {
                _uiState.value = BlockDashboardUiState.Error("Erro ao carregar regras: ${e.message}")
            }
        }
    }

    fun onRuleEnabledChange(ruleId: Long, isEnabled: Boolean) {
        viewModelScope.launch {
            try {
                updateRuleEnabledUseCase(ruleId, isEnabled)
            } catch (e: Exception) {

            }
        }
    }


    fun deleteRule(ruleId: Long) {
        viewModelScope.launch {
            _uiState.value = BlockDashboardUiState.Loading
            try {
                deleteBlockRuleUseCase(ruleId)
                _uiState.value = BlockDashboardUiState.Success
            } catch (e: Exception) {
                _uiState.value = BlockDashboardUiState.Error("Erro ao excluir regra: ${e.message}")
            }
        }
    }


    suspend fun getFullRuleById(ruleId: Long): com.motoacademy.android.qiet.domain.model.BlockRule? {
        return try {
            val allRules = getAllBlockRulesUseCase().first()
            allRules.find { it.id == ruleId }?.toModel()
        } catch (e: Exception) {
            null
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
            else -> sortedDays.joinToString(", ") {
                it.name.substring(0,3).lowercase().replaceFirstChar(Char::titlecase)
            }
        }
    }

    fun updateRule(
        ruleId: Long,
        ruleName: String,
        isEnabled: Boolean,
        prefixList: List<String>,
        timeRestrictionEnabled: Boolean,
        startTime: String?,
        endTime: String?,
        selectedDays: List<DayOfWeek>?
    ) {
        viewModelScope.launch {
            println("\n🔄 [ViewModel] ===== Salvando =====")
            println("🔄 [ViewModel] ID da regra: $ruleId")
            println("🔄 [ViewModel] Novo nome: '$ruleName'")
            println("🔄 [ViewModel] Status: $isEnabled")
            println("🔄 [ViewModel] Prefixos: $prefixList")

            _uiState.value = BlockDashboardUiState.Loading

            try {
                val existingRule = getFullRuleById(ruleId)

                if (existingRule == null) {
                    println("❌ [ViewModel] Regra $ruleId não encontrada no banco!")
                    _uiState.value = BlockDashboardUiState.Error("Regra não encontrada")
                    return@launch
                }

                println("✅ [ViewModel] Regra encontrada:")
                println("✅ [ViewModel]   Nome atual: '${existingRule.ruleName}'")
                println("✅ [ViewModel]   Status atual: ${existingRule.isEnabled}")

                val updatedRule = existingRule.copy(
                    ruleName = ruleName,
                    isEnabled = isEnabled,
                    blockedRegexRules = prefixList,
                    interval = if (timeRestrictionEnabled && startTime != null && endTime != null && selectedDays != null) {
                        com.motoacademy.android.qiet.data.local.model.IntervalTime(
                            startTime = startTime,
                            endTime = endTime,
                            daysOfWeek = selectedDays
                        )
                    } else null,
                    updatedAt = System.currentTimeMillis()
                )

                println("🔄 [ViewModel] Regra preparada para salvar:")
                println("🔄 [ViewModel]   Novo nome: '${updatedRule.ruleName}'")
                println("🔄 [ViewModel]   Novo status: ${updatedRule.isEnabled}")
                println("🔄 [ViewModel]   Timestamp: ${updatedRule.updatedAt}")

                println("🔥 [ViewModel] Chamando updateBlockRuleUseCase...")
                updateBlockRuleUseCase(updatedRule)
                println("✅ [ViewModel] updateBlockRuleUseCase concluído")

                updateLocalRule(updatedRule)

                _uiState.value = BlockDashboardUiState.Success

                println("🎉 [ViewModel] ===== SALVAMENTO CONCLUÍDO =====")
                println("🎉 [ViewModel] Alterações processadas com sucesso!")

            } catch (e: Exception) {
                println("💥 [ViewModel] ERRO ao salvar: ${e.message}")
                e.printStackTrace()
                _uiState.value = BlockDashboardUiState.Error("Erro ao salvar: ${e.message}")
            }
        }
    }

    private fun updateLocalRule(updatedRule: com.motoacademy.android.qiet.domain.model.BlockRule) {
        // Atualiza na lista local
        _rules.value = _rules.value.map { rule ->
            if (rule.id == updatedRule.id) {
                rule.copy(
                    title = updatedRule.ruleName,
                    isChecked = updatedRule.isEnabled,
                    prefixLabel = if (updatedRule.blockedRegexRules.isNotEmpty())
                        "Prefixo(s): ${updatedRule.blockedRegexRules.joinToString(", ")}"
                    else null,
                    blockInterval = updatedRule.interval?.let { interval ->
                        "Bloqueio: ${interval.startTime} - ${interval.endTime}"
                    },
                    blockWeek = updatedRule.interval?.daysOfWeek?.let { days ->
                        formatSelectedDays(days)
                    }
                )
            } else {
                rule
            }
        }
        println("✅ [ViewModel] Lista atualizada")
    }

    suspend fun verifyRuleWasSaved(ruleId: Long, expectedName: String): Boolean {
        println("\n🔍 [ViewModel] VERIFICANDO se regra foi salva...")

        val ruleInDb = getFullRuleById(ruleId)

        if (ruleInDb == null) {
            println("❌ [ViewModel] Regra $ruleId não existe no banco!")
            return false
        }

        println("🔍 [ViewModel] Regra no banco:")
        println("🔍 [ViewModel]   ID: ${ruleInDb.id}")
        println("🔍 [ViewModel]   Nome: '${ruleInDb.ruleName}'")
        println("🔍 [ViewModel]   Esperado: '$expectedName'")
        println("🔍 [ViewModel]   Status: ${ruleInDb.isEnabled}")
        println("🔍 [ViewModel]   Última atualização: ${ruleInDb.updatedAt}")

        val isSaved = ruleInDb.ruleName == expectedName

        if (isSaved) {
            println("✅ [ViewModel] VERIFICAÇÃO: Regra FOI salva corretamente!")
        } else {
            println("❌ [ViewModel] VERIFICAÇÃO: Regra NÃO foi salva!")
            println("❌ [ViewModel] Diferença no nome!")
        }

        return isSaved
    }
}