package com.motoacademy.android.qiet.domain.repository

import kotlinx.coroutines.flow.Flow
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity
import com.motoacademy.android.qiet.data.local.model.IntervalTime
import com.motoacademy.android.qiet.data.local.model.BlockedContact
import com.motoacademy.android.qiet.domain.model.BlockedCallSpam
import com.motoacademy.android.qiet.domain.model.BlockRule // ADICIONE ESTA IMPORT

interface BlockRuleRepository {
    // Leitura
    fun getAllRules(): Flow<List<BlockRuleEntity>>
    fun getAllBlockedCalls(): Flow<List<BlockedCallSpam>>
    suspend fun getRuleById(id: Long): BlockRuleEntity?

    // Inserção/Atualização
    suspend fun addOrUpdateRule(rule: BlockRuleEntity): Long
    suspend fun addCallHistory(callSpam: BlockedCallSpam): Long

    // Atualizações específicas
    suspend fun updateBlockedContacts(ruleId: Long, contacts: List<BlockedContact>)
    suspend fun updateBlockedRegexRules(ruleId: Long, regexRules: List<String>)
    suspend fun updateInterval(ruleId: Long, interval: IntervalTime?)
    suspend fun updateEnabled(ruleId: Long, enabled: Boolean)

    // Exclusão
    suspend fun deleteRuleById(id: Long)

    // Métodos adicionais úteis (opcional)
    suspend fun deleteAllRules()
    suspend fun deleteAllCallHistory()
    suspend fun getRulesCount(): Int

    // NOVO MÉTODO: Adicionado
    suspend fun updateBlockRule(blockRule: BlockRule)
}