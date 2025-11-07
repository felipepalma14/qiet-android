package com.motoacademy.android.qiet.domain.usecase

import com.motoacademy.android.qiet.domain.model.BlockRule
import com.motoacademy.android.qiet.data.mapper.toEntity
import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import javax.inject.Inject

class AddBlockRuleUseCase @Inject constructor(
    private val repository: BlockRuleRepository
) {
    suspend operator fun invoke(rule: BlockRule): Long {
        return repository.addOrUpdateRule(rule.toEntity())
    }
}