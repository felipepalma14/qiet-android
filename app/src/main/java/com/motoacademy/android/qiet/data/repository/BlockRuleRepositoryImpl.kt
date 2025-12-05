package com.motoacademy.android.qiet.data.repository

import com.motoacademy.android.qiet.domain.model.BlockRule
import com.motoacademy.android.qiet.data.local.dao.BlockRuleDao
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import com.motoacademy.android.qiet.data.local.model.BlockedContact
import com.motoacademy.android.qiet.data.local.model.IntervalTime
import com.motoacademy.android.qiet.domain.model.BlockedCallSpam
import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import javax.inject.Inject

class BlockRuleRepositoryImpl @Inject constructor(
    private val dao: BlockRuleDao
) : BlockRuleRepository {

    init {
        println("DEBUG: [Repository] BlockRuleRepositoryImpl (Room) inicializado")
    }

    override fun getAllRules(): Flow<List<BlockRuleEntity>> {
        println("DEBUG: [Repository] getAllRules chamado")
        return dao.getAll()
    }

    override fun getAllBlockedCalls(): Flow<List<BlockedCallSpam>> {
        return kotlinx.coroutines.flow.flowOf(emptyList())
    }

    override suspend fun getRuleById(id: Long): BlockRuleEntity? {
        return dao.getById(id)
    }

    override suspend fun addOrUpdateRule(rule: BlockRuleEntity): Long {
        println("DEBUG: [Repository] addOrUpdateRule: '${rule.ruleName}'")
        return dao.insert(rule)
    }

    override suspend fun addCallHistory(callSpam: BlockedCallSpam): Long = 1L

    override suspend fun deleteRuleById(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun updateBlockedContacts(ruleId: Long, contacts: List<BlockedContact>) {
        dao.updateBlockedContacts(ruleId, contacts, System.currentTimeMillis())
    }

    override suspend fun updateBlockedRegexRules(ruleId: Long, regexRules: List<String>) {
        dao.updateBlockedRegexRules(ruleId, regexRules, System.currentTimeMillis())
    }

    override suspend fun updateInterval(ruleId: Long, interval: IntervalTime?) {
        dao.updateInterval(ruleId, interval, System.currentTimeMillis())
    }

    override suspend fun updateEnabled(ruleId: Long, enabled: Boolean) {
        dao.updateEnabled(ruleId, enabled, System.currentTimeMillis())
    }

    override suspend fun deleteAllRules() {
        val allRules = dao.getAll().first()
        allRules.forEach { rule ->
            dao.deleteById(rule.id)
        }
    }

    override suspend fun deleteAllCallHistory() {

    }

    override suspend fun getRulesCount(): Int {
        return dao.getAll().first().size
    }

    override suspend fun updateBlockRule(blockRule: BlockRule) {
        println("\n=== DEBUG: [Repository] updateBlockRule INÍCIO ===")
        println("DEBUG: [Repository] ID: ${blockRule.id}")
        println("DEBUG: [Repository] Nome: '${blockRule.ruleName}'")
        println("DEBUG: [Repository] Status: ${blockRule.isEnabled}")
        println("DEBUG: [Repository] Prefixos: ${blockRule.blockedRegexRules}")
        println("DEBUG: [Repository] Contatos: ${blockRule.blockedContacts.size}")
        println("DEBUG: [Repository] UpdatedAt: ${blockRule.updatedAt}")

        try {

            val existingRule = dao.getById(blockRule.id)
            println("DEBUG: [Repository] Regra existe no banco? ${existingRule != null}")

            if (existingRule != null) {
                println("DEBUG: [Repository] Nome atual no banco: '${existingRule.ruleName}'")
            }


            println("DEBUG: [Repository] Executando updateFullBlockRule...")
            dao.updateFullBlockRule(
                id = blockRule.id,
                ruleName = blockRule.ruleName,
                isEnabled = blockRule.isEnabled,
                blockedContacts = blockRule.blockedContacts,
                blockedRegexRules = blockRule.blockedRegexRules,
                interval = blockRule.interval,
                updatedAt = blockRule.updatedAt
            )

            println("DEBUG: [Repository] updateFullBlockRule executado com sucesso!")


            val afterUpdate = dao.getById(blockRule.id)
            if (afterUpdate?.ruleName == blockRule.ruleName) {
                println("✅ SUCESSO: [Repository] Regra '${blockRule.ruleName}' atualizada no banco!")
                println("✅ Nome verificado: '${afterUpdate.ruleName}'")
                println("✅ Status verificado: ${afterUpdate.isEnabled}")
            } else {
                println("❌ ERRO: [Repository] Update NÃO funcionou!")
                println("❌ Esperado: '${blockRule.ruleName}'")
                println("❌ Obtido: '${afterUpdate?.ruleName ?: "null"}'")
            }

        } catch (e: Exception) {
            println(" ERRO : ${e.message}")
            e.printStackTrace()


            try {
                println("DEBUG: [Repository] Tentando fallback com insert...")
                val existingEntity = dao.getById(blockRule.id)

                val entity = BlockRuleEntity(
                    id = blockRule.id,
                    ruleName = blockRule.ruleName,
                    isEnabled = blockRule.isEnabled,
                    color = existingEntity?.color ?: getColorForRule(blockRule),
                    blockedContacts = blockRule.blockedContacts,
                    blockedRegexRules = blockRule.blockedRegexRules,
                    interval = blockRule.interval,
                    createdAt = existingEntity?.createdAt ?: blockRule.createdAt,
                    updatedAt = blockRule.updatedAt
                )

                val result = dao.insert(entity)
                println(": $result")


                val afterFallback = dao.getById(blockRule.id)
                if (afterFallback?.ruleName == blockRule.ruleName) {
                    println("")
                }

            } catch (fallbackError: Exception) {
                println("❌ Fallback também falhou: ${fallbackError.message}")
            }
        }

        println("=== DEBUG: [Repository] updateBlockRule FIM ===\n")
    }

    private fun getColorForRule(blockRule: BlockRule): String {
        return when {
            blockRule.ruleName.contains("Telemarketing", ignoreCase = true) -> "#FF5252"
            blockRule.ruleName.contains("Spam", ignoreCase = true) -> "#FF9800"
            blockRule.ruleName.contains("Trabalho", ignoreCase = true) -> "#2196F3"
            blockRule.ruleName.contains("Noturno", ignoreCase = true) -> "#673AB7"
            blockRule.ruleName.contains("Internacional", ignoreCase = true) -> "#4CAF50"
            else -> "#607D8B"
        }
    }
}