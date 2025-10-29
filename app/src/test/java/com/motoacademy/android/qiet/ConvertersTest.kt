package com.motoacademy.android.qiet


import com.motoacademy.android.qiet.data.local.converter.BlockedContactConverter
import com.motoacademy.android.qiet.data.local.converter.IntervalTimeConverter
import com.motoacademy.android.qiet.data.local.converter.StringListConverter

import com.motoacademy.android.qiet.data.local.model.BlockedContact
import com.motoacademy.android.qiet.data.local.model.IntervalTime
import com.motoacademy.android.qiet.data.local.model.DayOfWeek

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ConvertersTest {
    private val blockedContactConverter = BlockedContactConverter()
    private val intervalTimeConverter = IntervalTimeConverter()
    private val stringListConverter = StringListConverter()

    // ---------- BlockedContactConverter ----------
    @Test
    fun fromBlockedContactListToJson_withNull_returnsNull() {
        val json = blockedContactConverter.fromBlockedContactListToJson(null)
        assertNull(json)
    }

    @Test
    fun fromBlockedContactListToJson_withEmptyList_returnsValidJson() {
        val json = blockedContactConverter.fromBlockedContactListToJson(emptyList())
        assertEquals("[]", json)
    }

    @Test
    fun fromJsonToBlockedContactList_withNull_returnsEmptyList() {
        val result = blockedContactConverter.fromJsonToBlockedContactList(null)
        assertTrue(result.isEmpty())
    }

    @Test
    fun fromJsonToBlockedContactList_withEmptyString_returnsEmptyList() {
        val result = blockedContactConverter.fromJsonToBlockedContactList("")
        assertTrue(result.isEmpty())
    }

    @Test
    fun fromJsonToBlockedContactList_withInvalidJson_returnsEmptyList() {
        val result = blockedContactConverter.fromJsonToBlockedContactList("invalid json")
        assertTrue(result.isEmpty())
    }

    @Test
    fun fromJsonToBlockedContactList_withValidJson_returnsList() {
        val json = """[{"phoneNumber":"123","displayName":"Test"}]"""
        val result = blockedContactConverter.fromJsonToBlockedContactList(json)
        assertEquals(1, result.size)
        assertEquals("123", result[0].phoneNumber)
        assertEquals("Test", result[0].displayName)
    }

    @Test
    fun blockedContact_roundTrip_isCorrect() {
        val original = listOf(
            BlockedContact("123", "Test 1"),
            BlockedContact("456", "Test 2")
        )

        val json = blockedContactConverter.fromBlockedContactListToJson(original)
        val result = blockedContactConverter.fromJsonToBlockedContactList(json)

        assertEquals(original, result)
    }

    // ---------- IntervalTimeConverter ----------
    @Test
    fun fromIntervalToJson_withNullInterval_returnsNull() {
        val json = intervalTimeConverter.fromIntervalToJson(null)
        assertNull(json) // Espera null REAL, não string
    }

    @Test
    fun fromJsonToInterval_withNullString_returnsNull() {
        val interval = intervalTimeConverter.fromJsonToInterval(null)
        assertNull(interval)
    }

    @Test
    fun fromJsonToInterval_withEmptyString_returnsNull() {
        val interval = intervalTimeConverter.fromJsonToInterval("")
        assertNull(interval)
    }

    @Test
    fun fromJsonToInterval_withBlankString_returnsNull() {
        val interval = intervalTimeConverter.fromJsonToInterval("   ")
        assertNull(interval)
    }

    @Test
    fun fromJsonToInterval_withValidJsonInterval_isCorrect() {
        val json = """{"startTime":"08:00","endTime":"17:00","daysOfWeek":["MONDAY","FRIDAY"]}"""

        val interval = intervalTimeConverter.fromJsonToInterval(json)

        requireNotNull(interval)
        assertEquals("08:00", interval.startTime)
        assertEquals("17:00", interval.endTime)
        assertEquals(listOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY), interval.daysOfWeek)
    }

    @Test
    fun intervalTime_roundTrip_isCorrect() {
        val original = IntervalTime(
            startTime = "09:30",
            endTime = "18:45",
            daysOfWeek = listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY)
        )

        val json = intervalTimeConverter.fromIntervalToJson(original)
        val result = intervalTimeConverter.fromJsonToInterval(json)

        assertNotNull(json) // JSON não deve ser null para objeto não-nulo
        assertNotNull(result) // Resultado deve ser igual ao original
        assertEquals(original, result)
    }

    @Test
    fun intervalTime_roundTrip_withNull_isCorrect() {
        val original = null

        val json = intervalTimeConverter.fromIntervalToJson(original)
        val result = intervalTimeConverter.fromJsonToInterval(json)

        assertNull(json) // JSON deve ser null para objeto null
        assertNull(result) // Resultado deve ser null
    }

    @Test
    fun fromJsonToInterval_withInvalidJson_returnsNull() {
        val interval = intervalTimeConverter.fromJsonToInterval("invalid json")
        assertNull(interval)
    }

    // ---------- StringListConverter ----------
    @Test
    fun fromStringListToJson_withNull_returnsNull() {
        val json = stringListConverter.fromStringListToJson(null)
        assertNull(json)
    }

    @Test
    fun fromStringListToJson_withEmptyList_returnsValidJson() {
        val json = stringListConverter.fromStringListToJson(emptyList())
        assertEquals("[]", json)
    }

    @Test
    fun fromJsonToStringList_withNull_returnsEmptyList() {
        val result = stringListConverter.fromJsonToStringList(null)
        assertTrue(result.isEmpty())
    }

    @Test
    fun fromJsonToStringList_withEmptyString_returnsEmptyList() {
        val result = stringListConverter.fromJsonToStringList("")
        assertTrue(result.isEmpty())
    }

    @Test
    fun fromJsonToStringList_withInvalidJson_returnsEmptyList() {
        val result = stringListConverter.fromJsonToStringList("invalid json")
        assertTrue(result.isEmpty())
    }

    @Test
    fun fromJsonToStringList_withValidJson_returnsList() {
        val json = """["item1", "item2", "item3"]"""
        val result = stringListConverter.fromJsonToStringList(json)
        assertEquals(listOf("item1", "item2", "item3"), result)
    }

    @Test
    fun stringList_roundTrip_isCorrect() {
        val original = listOf("apple", "banana", "cherry")

        val json = stringListConverter.fromStringListToJson(original)
        val result = stringListConverter.fromJsonToStringList(json)

        assertEquals(original, result)
    }
}
