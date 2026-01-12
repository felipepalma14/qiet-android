package com.motoacademy.android.qiet.services

import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity
import com.motoacademy.android.qiet.data.local.model.toAppDayOfWeek
import com.motoacademy.android.qiet.domain.model.BlockedCallSpam
import com.motoacademy.android.qiet.domain.usecase.GetAllBlockRulesUseCase
import com.motoacademy.android.qiet.domain.usecase.SaveBlockedCallUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@AndroidEntryPoint
class SpamCallScreeningService : CallScreeningService() {

    private val serviceJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + serviceJob)

    // Repositório / use case simulado (injeção via Hilt recomendada)

    @Inject
    lateinit var getAllBlockRulesUseCase: GetAllBlockRulesUseCase
    @Inject
    lateinit var saveBlockedCallUseCase: SaveBlockedCallUseCase

    //var spamRepo: GetAllBlockRulesUseCase = GetAllBlockRulesUseCase(BlockRuleFakeRepositoryImpl())

    // Callback ou repositório de histórico (simulado)
    //var saveBlockedCallUseCase: SaveBlockedCallUseCase = SaveBlockedCallUseCase(BlockRuleFakeRepositoryImpl())

    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = callDetails.handle?.schemeSpecificPart ?: return
        Log.d("SpamBlocker", "📞 Chamada recebida: $phoneNumber")

        scope.launch {
            var shouldBlock = false
            var matchedRule: BlockRuleEntity? = null

            getAllBlockRulesUseCase().collect { rules ->
                val enabledRules = rules.filter { it.isActiveNow() }

                for (rule in enabledRules) {
                    Log.d("SpamBlocker", "🔍 Verificando regra: ${rule.ruleName}")
                    rule.interval?.daysOfWeek
                    rule.interval?.startTime
                    rule.interval?.endTime

                    val contactMatch = rule.blockedContacts.any {
                        it.phoneNumber == phoneNumber
                    }

                    val regexMatch = rule.blockedRegexRules.any { regex ->
                        phoneNumber.contains(regex)
                    }

                    if (contactMatch || regexMatch) {
                        shouldBlock = true
                        matchedRule = rule
                        Log.d(
                            "SpamEvaluator",
                            "🚫 Número $phoneNumber bloqueado pela regra '${rule.ruleName}'"
                        )
                        break
                    }
                }

                if (shouldBlock && matchedRule != null) {
                    // 🧱 Cria o registro da chamada bloqueada
                    val blockedCall = BlockedCallSpam(
                        id = 0L,
                        name = matchedRule.ruleName,
                        number = phoneNumber,
                        reason = matchedRule.ruleName,
                        createdAt = System.currentTimeMillis()
                    )

                    Log.d("SpamBlocker", "💾 Salvando chamada bloqueada: $blockedCall")

                    // Exemplo: salvar no banco ou repositório
                    saveBlockedCallUseCase(blockedCall)

                    // 🔕 Bloqueia / silencia a chamada
                    respondToCall(
                        callDetails,
                        CallResponse.Builder()
                            .setDisallowCall(true)
                            .setRejectCall(true)
                            .setSkipCallLog(true)
                            .setSkipNotification(true)
                            .build()
                    )
                } else {
                    Log.d("SpamBlocker", "✅ Número permitido: $phoneNumber")
                    respondToCall(
                        callDetails,
                        CallResponse.Builder()
                            .setDisallowCall(false)
                            .build()
                    )
                }

                // Sai do fluxo após processar uma vez
                cancel()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}

fun BlockRuleEntity.isActiveNow(
    now: ZonedDateTime = ZonedDateTime.now()
): Boolean {
    if (isEnabled.not()) return false
    val interval = interval ?: return false

    val today = now.dayOfWeek.toAppDayOfWeek()
    val currentTime = now.toLocalTime().toString()

    // 1️⃣ valida dia da semana
    if (
        interval.daysOfWeek.isNullOrEmpty() ||
        today !in interval.daysOfWeek
    ) return false

    val start = interval.startTime
    val end = interval.endTime

    // 2️⃣ valida horário
    return if (start <= end) {
        // intervalo normal (ex: 08:00 - 18:00)
        currentTime in start..end
    } else {
        // intervalo cruza meia-noite (ex: 22:00 - 06:00)
        currentTime >= start || currentTime <= end
    }
}
