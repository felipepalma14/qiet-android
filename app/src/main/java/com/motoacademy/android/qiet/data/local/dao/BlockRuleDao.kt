package com.motoacademy.android.qiet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity

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
}


