package com.motoacademy.android.qiet

import org.junit.Test
import org.junit.Assert.*

import com.motoacademy.android.qiet.data.local.model.DayOfWeek

class DayOfWeekTest {

    @Test
    fun dayOfWeek_values_containsAllDays() {
        val days = DayOfWeek.values()

        assertEquals(7, days.size)
        assertEquals(DayOfWeek.MONDAY, days[0])
        assertEquals(DayOfWeek.SUNDAY, days[6])
    }

    @Test
    fun dayOfWeek_valueOf_worksCorrectly() {
        assertEquals(DayOfWeek.MONDAY, DayOfWeek.valueOf("MONDAY"))
        assertEquals(DayOfWeek.FRIDAY, DayOfWeek.valueOf("FRIDAY"))
    }

    @Test
    fun dayOfWeek_ordinal_hasCorrectOrder() {
        assertEquals(0, DayOfWeek.MONDAY.ordinal)
        assertEquals(6, DayOfWeek.SUNDAY.ordinal)
    }
}