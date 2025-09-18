package com.motoacademy.android.qiet.domain.repository

import kotlinx.coroutines.flow.Flow
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity
import com.motoacademy.android.qiet.data.local.model.IntervalTime
import com.motoacademy.android.qiet.data.local.model.BlockedContact
interface BlockRuleRepository {
    fun getAllRules(): Flow<List<BlockRuleEntity>>
    suspend fun getRuleById(id: Long): BlockRuleEntity?
    suspend fun addRule(rule: BlockRuleEntity): Long
    suspend fun updateRule(rule: BlockRuleEntity)
    suspend fun deleteRule(rule: BlockRuleEntity)

    suspend fun updateBlockedContacts(ruleId: Long, contacts: List<BlockedContact>)
    suspend fun updateBlockedRegexRules(ruleId: Long, regexRules: List<String>)
    suspend fun updateInterval(ruleId: Long, interval: IntervalTime?)
    suspend fun updateEnabled(ruleId: Long, enabled: Boolean)
}
