package com.motoacademy.android.qiet.domain.usecase

import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import javax.inject.Inject

class UpdateRuleEnabledUseCase @Inject constructor(
    private val repository: BlockRuleRepository
) {
    suspend operator fun invoke(ruleId: Long, enabled: Boolean) {
        repository.updateEnabled(ruleId, enabled)
    }
}
