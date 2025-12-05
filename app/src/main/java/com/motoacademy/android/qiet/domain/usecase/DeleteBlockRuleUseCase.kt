// DeleteBlockRuleUseCase.kt
package com.motoacademy.android.qiet.domain.usecase

import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import javax.inject.Inject

class DeleteBlockRuleUseCase @Inject constructor(
    private val repository: BlockRuleRepository
) {
    suspend operator fun invoke(ruleId: Long) {
        repository.deleteRuleById(ruleId)
    }
}