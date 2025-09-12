package com.motoacademy.android.qiet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity

@Dao
interface BlockRuleDao {

    //insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: BlockRuleEntity): Long //retorna o id da regra adicionada

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rules:List<BlockRuleEntity>): List<Long> //retorna lista de ids das regras

    //select
    @Query("SELECT * FROM block_rule")
    fun getAllWithFlow(): Flow<List<BlockRuleEntity>> //pedir explicacao para felipe

    @Query("SELECT * FROM block_rule")
    suspend fun getAllOnce(): List<BlockRuleEntity>


    //delete

    //update
}