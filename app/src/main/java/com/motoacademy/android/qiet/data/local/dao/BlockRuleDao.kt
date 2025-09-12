package com.motoacademy.android.qiet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity

@Dao
interface BlockRuleDao {

    //insert
   // @Insert(onConflict = onConflictStrategy.REPLACE)
    suspend fun insert(rule: BlockRuleEntity)

    //select

    //delete

    //update
}