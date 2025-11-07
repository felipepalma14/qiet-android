package com.motoacademy.android.qiet.domain.usecase

import com.motoacademy.android.qiet.domain.model.BlockedCallSpam
import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import javax.inject.Inject

class SaveBlockedCallUseCase @Inject constructor(
    private val repository: BlockRuleRepository,
) {
    suspend operator fun invoke(blockedCallSpam: BlockedCallSpam): Long {
        return repository.addCallHistory(blockedCallSpam)
    }
}