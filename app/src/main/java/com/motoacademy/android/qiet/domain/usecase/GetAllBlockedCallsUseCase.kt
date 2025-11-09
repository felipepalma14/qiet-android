package com.motoacademy.android.qiet.domain.usecase

import com.motoacademy.android.qiet.domain.model.BlockedCallSpam
import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import com.motoacademy.android.qiet.features.call_history.model.BlockedCallSpamUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject

class GetAllBlockedCallsUseCase @Inject constructor(
    private val repository: BlockRuleRepository
) {
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val locale = Locale("pt", "BR")

    operator fun invoke(): Flow<List<BlockedCallSpamUi>> =
        repository.getAllBlockedCalls().map { calls ->
            val now = LocalDate.now()
            val locale = Locale("pt", "BR")
            val currentWeek = now.get(WeekFields.of(locale).weekOfWeekBasedYear())
            val currentYear = now.year

            calls.map { call ->
                val callDate = Instant.ofEpochMilli(call.createdAt)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                val callWeek = callDate.get(WeekFields.of(locale).weekOfWeekBasedYear())

                val formatted = when {
                    // 1️⃣ Hoje
                    callDate.isEqual(now) -> "Hoje"

                    // 2️⃣ Mesma semana e até sexta-feira
                    callWeek == currentWeek && callDate.year == currentYear &&
                            callDate.dayOfWeek <= DayOfWeek.FRIDAY -> {
                        callDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, locale)
                            .replaceFirstChar { it.uppercase() } // Segunda, Terça...
                    }

                    // 3️⃣ Fora da semana atual → data formatada
                    else -> callDate.format(formatter)
                }

                BlockedCallSpamUi(
                    id = call.id,
                    name = call.name,
                    number = call.number,
                    reason = call.reason,
                    createdAt = call.createdAt,
                    formattedDate = formatted
                )
            }.sortedByDescending { it.createdAt } // opcional: mais recentes primeiro
        }
    }

