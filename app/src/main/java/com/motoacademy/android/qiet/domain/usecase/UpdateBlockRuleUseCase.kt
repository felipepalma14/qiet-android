package com.motoacademy.android.qiet.domain.usecase

import com.motoacademy.android.qiet.domain.model.BlockRule
import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import javax.inject.Inject

class UpdateBlockRuleUseCase @Inject constructor(
    private val repository: BlockRuleRepository
) {
    suspend operator fun invoke(blockRule: BlockRule) {
        println("\n=== DEBUG: [UseCase] UpdateBlockRuleUseCase chamado ===")
        println("DEBUG: [UseCase] ID da regra: ${blockRule.id}")
        println("DEBUG: [UseCase] Nome da regra: '${blockRule.ruleName}'")
        println("DEBUG: [UseCase] Status: ${blockRule.isEnabled}")
        println("DEBUG: [UseCase] Timestamp: ${blockRule.updatedAt}")

        try {
            println("DEBUG: [UseCase] Chamando repository.updateBlockRule...")
            repository.updateBlockRule(blockRule)
            println("SUCESSO: [UseCase] Regra atualizada no repositório")
        } catch (e: Exception) {
            println("ERRO: [UseCase] Falha no repositório: ${e.message}")
            e.printStackTrace()
            throw e
        }

        println("=== DEBUG: [UseCase] UpdateBlockRuleUseCase concluído ===\n")
    }
}