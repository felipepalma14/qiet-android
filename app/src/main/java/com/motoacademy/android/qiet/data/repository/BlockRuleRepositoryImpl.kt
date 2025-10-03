package com.motoacademy.android.qiet.data.repository

import com.motoacademy.android.qiet.data.local.dao.BlockRuleDao
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity
import kotlinx.coroutines.flow.Flow
import com.motoacademy.android.qiet.data.local.model.BlockedContact
import com.motoacademy.android.qiet.data.local.model.IntervalTime
import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository

class BlockRuleRepositoryImpl(
    private val dao: BlockRuleDao
) : BlockRuleRepository {

    override fun getAllRules(): Flow<List<BlockRuleEntity>> = dao.getAll()

    override suspend fun getRuleById(id: Long): BlockRuleEntity? = dao.getById(id)

    override suspend fun addOrUpdateRule(rule: BlockRuleEntity): Long = dao.insert(rule)

    override suspend fun deleteRuleById(id: Long) = dao.deleteById(id)

    override suspend fun updateBlockedContacts(ruleId: Long, contacts: List<BlockedContact>) {
        dao.updateBlockedContacts(ruleId, contacts)
    }

    override suspend fun updateBlockedRegexRules(ruleId: Long, regexRules: List<String>) {
        dao.updateBlockedRegexRules(ruleId, regexRules)
    }

    override suspend fun updateInterval(ruleId: Long, interval: IntervalTime?) {
        dao.updateInterval(ruleId, interval)
    }

    override suspend fun updateEnabled(ruleId: Long, enabled: Boolean) {
        dao.updateEnabled(ruleId, enabled)
    }
}



