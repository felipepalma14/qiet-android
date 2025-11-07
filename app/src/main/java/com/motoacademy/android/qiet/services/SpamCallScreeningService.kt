package com.motoacademy.android.qiet.services

import android.net.Uri
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity
import com.motoacademy.android.qiet.data.repository.BlockRuleFakeRepositoryImpl
import com.motoacademy.android.qiet.data.repository.BlockRuleRepositoryImpl
import com.motoacademy.android.qiet.domain.model.BlockedCallSpam
import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import com.motoacademy.android.qiet.domain.usecase.GetAllBlockRulesUseCase
import com.motoacademy.android.qiet.domain.usecase.SaveBlockedCallUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject
@AndroidEntryPoint
class SpamCallScreeningService : CallScreeningService() {

    private val serviceJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + serviceJob)

    // Repositório / use case simulado (injeção via Hilt recomendada)
    var spamRepo: GetAllBlockRulesUseCase = GetAllBlockRulesUseCase(BlockRuleFakeRepositoryImpl())

    // Callback ou repositório de histórico (simulado)
    var blockedHistoryRepo: SaveBlockedCallUseCase = SaveBlockedCallUseCase(BlockRuleFakeRepositoryImpl())

    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = callDetails.handle?.schemeSpecificPart ?: return
        Log.d("SpamBlocker", "📞 Chamada recebida: $phoneNumber")

        scope.launch {
            var shouldBlock = false
            var matchedRule: BlockRuleEntity? = null

            spamRepo().collect { rules ->
                val enabledRules = rules.filter { it.isEnabled }

                for (rule in enabledRules) {
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
                    blockedHistoryRepo(blockedCall)

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

