package com.motoacademy.android.qiet

import org.junit.Test
import org.junit.Assert.*
import com.motoacademy.android.qiet.data.local.model.BlockedContact
import com.motoacademy.android.qiet.data.local.model.IntervalTime
import com.motoacademy.android.qiet.data.local.model.DayOfWeek

import com.motoacademy.android.qiet.domain.model.BlockRule
class BlockRuleTest {

    private val sampleBlockedContacts = listOf(
        BlockedContact("123456789", "John Doe"),
        BlockedContact("987654321", "Jane Smith")
    )

    private val sampleRegexRules = listOf("*spam*", "*ads*")

    private val sampleInterval = IntervalTime(
        startTime = "09:00",
        endTime = "17:00",
        daysOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)
    )

    private val sampleBlockRule = BlockRule(
        id = 1L,
        ruleName = "Test Rule",
        isEnabled = true,
        color = "#FF0000",
        blockedContacts = sampleBlockedContacts,
        blockedRegexRules = sampleRegexRules,
        interval = sampleInterval,
        createdAt = 1000L,
        updatedAt = 2000L
    )

    @Test
    fun blockRule_equals_withSameValues_returnsTrue() {
        val rule1 = sampleBlockRule
        val rule2 = sampleBlockRule.copy()

        assertEquals(rule1, rule2)
        assertEquals(rule1.hashCode(), rule2.hashCode())
    }

    @Test
    fun blockRule_equals_withDifferentValues_returnsFalse() {
        val rule1 = sampleBlockRule
        val rule2 = sampleBlockRule.copy(id = 2L)

        assertNotEquals(rule1, rule2)
    }

    @Test
    fun blockRule_copy_changesOnlySpecifiedFields() {
        val copied = sampleBlockRule.copy(
            ruleName = "Updated Rule",
            isEnabled = false
        )

        assertEquals(1L, copied.id)
        assertEquals("Updated Rule", copied.ruleName)
        assertFalse(copied.isEnabled)
        assertEquals("#FF0000", copied.color)
        assertEquals(sampleBlockedContacts, copied.blockedContacts)
        assertEquals(sampleRegexRules, copied.blockedRegexRules)
        assertEquals(sampleInterval, copied.interval)
        assertEquals(1000L, copied.createdAt)
        assertEquals(2000L, copied.updatedAt)
    }

    @Test
    fun blockRule_toString_containsRelevantInformation() {
        val string = sampleBlockRule.toString()

        assertTrue(string.contains("Test Rule"))
        assertTrue(string.contains("1"))
        assertTrue(string.contains("#FF0000"))
    }

    @Test
    fun blockRule_destructuring_worksCorrectly() {
        val (id, name, enabled, color, contacts, regex, interval, created, updated) = sampleBlockRule

        assertEquals(1L, id)
        assertEquals("Test Rule", name)
        assertTrue(enabled)
        assertEquals("#FF0000", color)
        assertEquals(sampleBlockedContacts, contacts)
        assertEquals(sampleRegexRules, regex)
        assertEquals(sampleInterval, interval)
        assertEquals(1000L, created)
        assertEquals(2000L, updated)
    }

    @Test
    fun blockRule_withNullInterval_createsSuccessfully() {
        val ruleWithoutInterval = BlockRule(
            id = 2L,
            ruleName = "No Interval Rule",
            isEnabled = true,
            color = "#00FF00",
            blockedContacts = emptyList(),
            blockedRegexRules = emptyList(),
            interval = null,
            createdAt = 3000L,
            updatedAt = 4000L
        )

        assertNull(ruleWithoutInterval.interval)
        assertEquals(emptyList<BlockedContact>(), ruleWithoutInterval.blockedContacts)
        assertEquals(emptyList<String>(), ruleWithoutInterval.blockedRegexRules)
    }

    @Test
    fun blockRule_withEmptyLists_createsSuccessfully() {
        val ruleWithEmptyLists = BlockRule(
            id = 3L,
            ruleName = "Empty Lists Rule",
            isEnabled = false,
            color = "#0000FF",
            blockedContacts = emptyList(),
            blockedRegexRules = emptyList(),
            interval = null,
            createdAt = 5000L,
            updatedAt = 6000L
        )

        assertTrue(ruleWithEmptyLists.blockedContacts.isEmpty())
        assertTrue(ruleWithEmptyLists.blockedRegexRules.isEmpty())
        assertFalse(ruleWithEmptyLists.isEnabled)
    }

    @Test
    fun blockRule_propertyAccess_returnsCorrectValues() {
        assertEquals(1L, sampleBlockRule.id)
        assertEquals("Test Rule", sampleBlockRule.ruleName)
        assertTrue(sampleBlockRule.isEnabled)
        assertEquals("#FF0000", sampleBlockRule.color)
        assertEquals(sampleBlockedContacts, sampleBlockRule.blockedContacts)
        assertEquals(sampleRegexRules, sampleBlockRule.blockedRegexRules)
        assertEquals(sampleInterval, sampleBlockRule.interval)
        assertEquals(1000L, sampleBlockRule.createdAt)
        assertEquals(2000L, sampleBlockRule.updatedAt)
    }
}