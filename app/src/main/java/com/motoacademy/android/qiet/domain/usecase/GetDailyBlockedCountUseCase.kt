package com.motoacademy.android.qiet.domain.usecase

import com.motoacademy.android.qiet.domain.model.BlockedCallSpam
import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject


class GetDailyBlockedCountUseCase @Inject constructor(
    private val repository: BlockRuleRepository
) {

    operator fun invoke(): Flow<List<BlockedCallSpam>> {
        val today = LocalDate.now()
        return repository.getAllBlockedCalls().map { calls ->
            calls.filter { call ->
                val callDate = Instant.ofEpochMilli(call.createdAt)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                callDate == today
            }
        }
    }
}