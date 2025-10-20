package com.motoacademy.android.qiet.domain.usecase

import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import javax.inject.Inject

class GetAllBlockRulesUseCase @Inject constructor(
    val repository: BlockRuleRepository
) {
    operator fun invoke() = repository.getAllRules()
}
