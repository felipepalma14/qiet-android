package com.motoacademy.android.qiet

import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity
import com.motoacademy.android.qiet.data.local.model.BlockedContact
import com.motoacademy.android.qiet.data.local.model.IntervalTime
import com.motoacademy.android.qiet.data.local.model.DayOfWeek
import org.junit.Assert.*
import org.junit.Test


class BlockRuleEntityTest {

    @Test
    fun entity_defaultValues_areCorrect() {
        val entity = BlockRuleEntity(
            ruleName = "Spam Rule",
            isEnabled = true,
            color = "red"
        )

        assertEquals("Spam Rule", entity.ruleName)
        assertTrue(entity.isEnabled)
        assertEquals("red", entity.color)
        assertTrue(entity.blockedContacts.isEmpty())
        assertTrue(entity.blockedRegexRules.isEmpty())
        assertNull(entity.interval)
        assertTrue(entity.createdAt > 0)
        assertTrue(entity.updatedAt > 0)
    }

    @Test
    fun entity_copy_updatesValuesCorrectly() {
        val entity = BlockRuleEntity(
            ruleName = "Rule 1",
            isEnabled = true,
            color = "blue"
        )

        val copy = entity.copy(
            ruleName = "Rule 2",
            isEnabled = false,
            color = "green"
        )

        assertEquals("Rule 2", copy.ruleName)
        assertFalse(copy.isEnabled)
        assertEquals("green", copy.color)
        assertEquals(entity.id, copy.id)
    }

    @Test
    fun entity_equality_worksCorrectly() {
        val entity1 = BlockRuleEntity(
            id = 1L,
            ruleName = "Rule",
            isEnabled = true,
            color = "blue"
        )
        val entity2 = BlockRuleEntity(
            id = 1L,
            ruleName = "Rule",
            isEnabled = true,
            color = "blue"
        )

        assertEquals(entity1, entity2)
        assertEquals(entity1.hashCode(), entity2.hashCode())
    }

    @Test
    fun entity_withBlockedContactsAndRegexRules_areStoredCorrectly() {
        val contact = BlockedContact(
            phoneNumber = "12345",
            displayName = "John"
        )
        val interval = IntervalTime(
            startTime = "09:00",
            endTime = "17:00",
            daysOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)
        )

        val entity = BlockRuleEntity(
            ruleName = "Work Hours Block",
            isEnabled = true,
            color = "yellow",
            blockedContacts = listOf(contact),
            blockedRegexRules = listOf(".*spam.*"),
            interval = interval
        )

        assertEquals(1, entity.blockedContacts.size)
        assertEquals("12345", entity.blockedContacts[0].phoneNumber)
        assertEquals("John", entity.blockedContacts[0].displayName)

        assertEquals(1, entity.blockedRegexRules.size)
        assertEquals(".*spam.*", entity.blockedRegexRules[0])

        assertNotNull(entity.interval)
        assertEquals("09:00", entity.interval?.startTime)
        assertEquals("17:00", entity.interval?.endTime)
        assertEquals(listOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY), entity.interval?.daysOfWeek)
    }
}

