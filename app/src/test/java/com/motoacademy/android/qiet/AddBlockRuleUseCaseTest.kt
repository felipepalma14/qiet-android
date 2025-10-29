package com.motoacademy.android.qiet

import com.motoacademy.android.qiet.domain.model.BlockRule
import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import com.motoacademy.android.qiet.domain.usecase.AddBlockRuleUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AddBlockRuleUseCaseTest {

    private lateinit var repository: BlockRuleRepository
    private lateinit var addBlockRuleUseCase: AddBlockRuleUseCase

    @Before
    fun setup() {
        repository = mockk()
        addBlockRuleUseCase = AddBlockRuleUseCase(repository)
    }

    @Test
    fun addBlockRule_correctlyMapsToEntityAndReturnsId() = runBlocking {
        // Arrange
        val rule = BlockRule(
            id = 0L,
            ruleName = "Test Rule",
            isEnabled = true,
            color = "#FF0000",
            blockedContacts = emptyList(),
            blockedRegexRules = listOf("123", "456"),
            interval = null,
            createdAt = 1000L,
            updatedAt = 2000L
        )

        val expectedId = 1L
        coEvery { repository.addOrUpdateRule(any()) } returns expectedId

        // Act
        val result = addBlockRuleUseCase(rule)

        // Assert
        assertEquals(expectedId, result)

        // Verifica se o mapper converteu corretamente BlockRule -> BlockRuleEntity
        coVerify {
            repository.addOrUpdateRule(match { entity ->
                entity.id == rule.id &&
                        entity.ruleName == rule.ruleName &&
                        entity.isEnabled == rule.isEnabled &&
                        entity.color == rule.color &&
                        entity.blockedContacts == rule.blockedContacts &&
                        entity.blockedRegexRules == rule.blockedRegexRules &&
                        entity.interval == rule.interval &&
                        entity.createdAt == rule.createdAt &&
                        entity.updatedAt == rule.updatedAt
            })
        }
    }
}

