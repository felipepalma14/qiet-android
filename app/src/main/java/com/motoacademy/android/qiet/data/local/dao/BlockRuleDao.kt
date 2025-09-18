package com.motoacademy.android.qiet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity
import com.motoacademy.android.qiet.data.local.model.BlockedContact
import com.motoacademy.android.qiet.data.local.model.IntervalTime

@Dao
interface BlockRuleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: BlockRuleEntity): Long

    @Update
    suspend fun update(rule: BlockRuleEntity)

    @Delete
    suspend fun delete(rule: BlockRuleEntity)

    @Query("SELECT * FROM block_rule WHERE id = :id")
    suspend fun getById(id: Long): BlockRuleEntity?

    @Query("SELECT * FROM block_rule")
    fun getAll(): Flow<List<BlockRuleEntity>>

    //Updates Parciais
    @Query("UPDATE block_rule SET blockedContacts = :contacts, updatedAt = :updatedAt WHERE id = :ruleId")
    suspend fun updateBlockedContacts(
        ruleId: Long,
        contacts: List<BlockedContact>,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("UPDATE block_rule SET blockedRegexRules = :regexRules, updatedAt = :updatedAt WHERE id = :ruleId")
    suspend fun updateBlockedRegexRules(
        ruleId: Long,
        regexRules: List<String>,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("UPDATE block_rule SET interval = :interval, updatedAt = :updatedAt WHERE id = :ruleId")
    suspend fun updateInterval(
        ruleId: Long,
        interval: IntervalTime?,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("UPDATE block_rule SET isEnabled = :enabled, updatedAt = :updatedAt WHERE id = :ruleId")
    suspend fun updateEnabled(
        ruleId: Long,
        enabled: Boolean,
        updatedAt: Long = System.currentTimeMillis()
    )
}


