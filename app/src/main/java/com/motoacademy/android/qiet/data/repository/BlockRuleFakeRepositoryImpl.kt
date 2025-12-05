package com.motoacademy.android.qiet.data.repository

import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity
import com.motoacademy.android.qiet.data.local.model.BlockedContact
import com.motoacademy.android.qiet.data.local.model.DayOfWeek
import com.motoacademy.android.qiet.data.local.model.IntervalTime
import com.motoacademy.android.qiet.domain.model.BlockedCallSpam
import com.motoacademy.android.qiet.domain.model.BlockRule // Importação adicionada
import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockRuleFakeRepositoryImpl @Inject constructor() : BlockRuleRepository {

    private val rules = MutableStateFlow<List<BlockRuleEntity>>(generateFakeRules())

    private val _blockedCall = MutableStateFlow<List<BlockedCallSpam>>(generateFakeBlockedCalls())

    override fun getAllRules(): Flow<List<BlockRuleEntity>> = rules

    override fun getAllBlockedCalls(): Flow<List<BlockedCallSpam>> = _blockedCall

    override suspend fun getRuleById(id: Long): BlockRuleEntity? {
        return rules.value.find { it.id == id }
    }

    override suspend fun addOrUpdateRule(rule: BlockRuleEntity): Long {
        val currentList = rules.value.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.id == rule.id }

        val newRule = rule.copy(
            updatedAt = System.currentTimeMillis(),
            id = if (rule.id == 0L) (currentList.maxOfOrNull { it.id } ?: 0L) + 1 else rule.id
        )

        if (existingIndex >= 0) {
            currentList[existingIndex] = newRule
        } else {
            currentList.add(newRule)
        }

        rules.value = currentList
        return newRule.id
    }

    override suspend fun addCallHistory(callSpam: BlockedCallSpam): Long {
        _blockedCall.update { current ->
            (current + callSpam.copy(id = (current.count() + 10).toLong())).sortedByDescending { it.createdAt }
        }
        return 1L
    }

    override suspend fun deleteRuleById(id: Long) {
        rules.value = rules.value.filterNot { it.id == id }
    }

    override suspend fun updateBlockedContacts(ruleId: Long, contacts: List<BlockedContact>) {
        updateRule(ruleId) { it.copy(blockedContacts = contacts) }
    }

    override suspend fun updateBlockedRegexRules(ruleId: Long, regexRules: List<String>) {
        updateRule(ruleId) { it.copy(blockedRegexRules = regexRules) }
    }

    override suspend fun updateInterval(ruleId: Long, interval: IntervalTime?) {
        updateRule(ruleId) { it.copy(interval = interval) }
    }

    override suspend fun updateEnabled(ruleId: Long, enabled: Boolean) {
        updateRule(ruleId) { it.copy(isEnabled = enabled) }
    }

    override suspend fun deleteAllRules() {
        rules.value = emptyList()
    }

    override suspend fun deleteAllCallHistory() {
        _blockedCall.value = emptyList()
    }

    override suspend fun getRulesCount(): Int {
        return rules.value.size
    }

    // NOVO MÉTODO IMPLEMENTADO
    override suspend fun updateBlockRule(blockRule: BlockRule) {
        // Primeiro busca a regra existente para preservar a cor
        val existingEntity = getRuleById(blockRule.id)
        val color = existingEntity?.color ?: "#FF5252"

        // Converte BlockRule (modelo de domínio) para BlockRuleEntity
        val entity = BlockRuleEntity(
            id = blockRule.id,
            ruleName = blockRule.ruleName,
            isEnabled = blockRule.isEnabled,
            color = color,
            blockedContacts = blockRule.blockedContacts,
            blockedRegexRules = blockRule.blockedRegexRules,
            interval = blockRule.interval,
            createdAt = blockRule.createdAt,
            updatedAt = blockRule.updatedAt
        )

        // Usa o método existente addOrUpdateRule
        addOrUpdateRule(entity)
    }

    private suspend fun updateRule(id: Long, transform: (BlockRuleEntity) -> BlockRuleEntity) {
        val updated = rules.value.map {
            if (it.id == id) transform(it).copy(updatedAt = System.currentTimeMillis()) else it
        }
        rules.value = updated
    }

    private fun generateFakeBlockedCalls(): List<BlockedCallSpam> {
        val now = System.currentTimeMillis()
        val yesterday = now - (24 * 60 * 60 * 1000)
        val any = 1761860768L
        val weekly = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        val monthly = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000)

        return listOf(
            BlockedCallSpam(
                1, "Telemarketing", "93922202", "Telemarketing", now
            ),
            BlockedCallSpam(
                2, "Spam Financeiro", "93922202", "Spam Financeiro", yesterday
            ),
            BlockedCallSpam(
                3, "Contatos suspeitos", "93922202", "Contatos suspeitos", weekly
            ),
            BlockedCallSpam(
                4, "Trabalho", "93922202", "xx", any
            ),
            BlockedCallSpam(
                5, "Trabalho", "93922202", "Sogra :(", any
            )
        )
    }

    private fun generateFakeRules(): List<BlockRuleEntity> {
        val now = System.currentTimeMillis()
        val weekdays = listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        )
        val weekend = listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        val allDays = weekdays + weekend

        return listOf(
            BlockRuleEntity(
                1, "Telemarketing", true, "#FF5252",
                blockedRegexRules = listOf("92001", "99302", "99304"),
                interval = IntervalTime("08:00", "20:00", allDays)
            ),
            BlockRuleEntity(
                2, "Spam Financeiro", true, "#FF9800",
                blockedRegexRules = listOf("0303"),
                interval = IntervalTime("09:00", "18:00", weekdays)
            ),
            BlockRuleEntity(
                3, "Contatos suspeitos", true, "#F44336",
                blockedContacts = listOf(BlockedContact("Scam 1", "+5511999999999")),
                interval = IntervalTime("00:00", "23:59", weekdays.dropLast(2))
            ),
            BlockRuleEntity(
                4, "Trabalho", false, "#2196F3",
                blockedContacts = listOf(
                    BlockedContact("Cliente A", "+5511888888888"),
                    BlockedContact("Cliente B", "+5511889999999"),
                ),
                interval = IntervalTime("22:00", "07:00", weekdays)
            ),
            BlockRuleEntity(
                5, "Bloqueio Noturno", true, "#673AB7",
                blockedRegexRules = listOf("(92) 993028102"),
                interval = IntervalTime("00:00", "06:00", weekdays)
            ),
            BlockRuleEntity(
                6, "Spam Internacional", true, "#4CAF50",
                blockedRegexRules = listOf("+44", "+1", "+91")
            ),
            BlockRuleEntity(
                9, "Fake Banks", true, "#607D8B",
                blockedRegexRules = listOf("(034)")
            ),
            BlockRuleEntity(
                10, "Sogra :C", true, "#9C27B0",
                blockedContacts = listOf(BlockedContact("Dona Maria", "+5511666666666"))
            )
        ).map { it.copy(createdAt = now, updatedAt = now) }
    }
}