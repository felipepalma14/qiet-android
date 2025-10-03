package com.motoacademy.android.qiet

import org.junit.Test
import org.junit.Assert.*

import com.motoacademy.android.qiet.data.local.model.DayOfWeek
import com.motoacademy.android.qiet.data.local.model.IntervalTime

class IntervalTimeTest {

    private val sampleDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

    @Test
    fun intervalTime_creation() {
        val interval = IntervalTime("09:00", "17:00", sampleDays)

        assertEquals("09:00", interval.startTime)
        assertEquals("17:00", interval.endTime)
        assertEquals(sampleDays, interval.daysOfWeek)
    }

    @Test
    fun intervalTime_equals_withSameValues() {
        val interval1 = IntervalTime("09:00", "17:00", sampleDays)
        val interval2 = IntervalTime("09:00", "17:00", sampleDays)

        assertEquals(interval1, interval2)
        assertEquals(interval1.hashCode(), interval2.hashCode())
    }

    @Test
    fun intervalTime_equals_differentValues() {
        val interval1 = IntervalTime("09:00", "17:00", sampleDays)
        val interval2 = IntervalTime("10:00", "18:00", sampleDays)

        assertNotEquals(interval1, interval2)
    }

    @Test
    fun intervalTime_copy_changesSpecifiedFields() {
        val original = IntervalTime("09:00", "17:00", sampleDays)
        val newDays = listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
        val copied = original.copy(startTime = "10:00", daysOfWeek = newDays)

        assertEquals("10:00", copied.startTime)
        assertEquals("17:00", copied.endTime)
        assertEquals(newDays, copied.daysOfWeek)
    }

    @Test
    fun intervalTime_toString_containsRelevantInfo() {
        val interval = IntervalTime("09:00", "17:00", sampleDays)
        val string = interval.toString()

        assertTrue(string.contains("09:00"))
        assertTrue(string.contains("17:00"))
    }

    @Test
    fun intervalTime_withEmptyDays() {
        val interval = IntervalTime("09:00", "17:00", emptyList())

        assertEquals("09:00", interval.startTime)
        assertTrue(interval.daysOfWeek.isEmpty())
    }
}