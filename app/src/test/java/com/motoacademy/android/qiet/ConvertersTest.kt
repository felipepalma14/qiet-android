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
import org.junit.Test

class ConvertersTest {
    private val blockedContactConverter = BlockedContactConverter()
    private val intervalTimeConverter = IntervalTimeConverter()
    private val stringListConverter = StringListConverter()

    // ---------- BlockedContactConverter ----------
    @Test
    fun fromBlockedContactListToJson_withValidList_isCorrect() {
        val contacts = listOf(
            BlockedContact("12345", "Alice"),
            BlockedContact("67890", "Bob")
        )

        val json = blockedContactConverter.fromBlockedContactListToJson(contacts)

        val expected =
            """[{"phoneNumber":"12345","displayName":"Alice"},{"phoneNumber":"67890","displayName":"Bob"}]"""
        assertEquals(expected, json)
    }

    @Test
    fun fromBlockedContactListToJson_withNullList_returnsEmptyJsonArray() {
        val json = blockedContactConverter.fromBlockedContactListToJson(null)
        assertEquals("[]", json)
    }

    @Test
    fun fromJsonToBlockedContactList_withValidJson_isCorrect() {
        val json =
            """[{"phoneNumber":"12345","displayName":"Alice"},{"phoneNumber":"67890","displayName":"Bob"}]"""

        val contacts = blockedContactConverter.fromJsonToBlockedContactList(json)

        assertEquals(2, contacts.size)
        assertEquals("12345", contacts[0].phoneNumber)
        assertEquals("Alice", contacts[0].displayName)
    }

    @Test
    fun blockedContact_roundTrip_isCorrect() {
        val original = listOf(
            BlockedContact("11111", "Carol"),
            BlockedContact("22222", null)
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
    fun fromStringListToJson_withValidList_isCorrect() {
        val list = listOf("a", "b", "c")
        val json = stringListConverter.fromStringListToJson(list)
        assertEquals("""["a","b","c"]""", json)
    }

    @Test
    fun fromStringListToJson_withNullList_returnsEmptyJsonArray() {
        val json = stringListConverter.fromStringListToJson(null)
        assertEquals("[]", json)
    }

    @Test
    fun fromJsonToStringList_withValidJson_isCorrect() {
        val json = """["x","y","z"]"""
        val list = stringListConverter.fromJsonToStringList(json)
        assertEquals(listOf("x", "y", "z"), list)
    }

    @Test
    fun fromJsonToStringList_withEmptyJsonArray_returnsEmptyList() {
        val json = "[]"
        val list = stringListConverter.fromJsonToStringList(json)
        assertEquals(emptyList<String>(), list)
    }

    @Test
    fun stringList_roundTrip_isCorrect() {
        val original = listOf("alpha", "beta", "gamma")

        val json = stringListConverter.fromStringListToJson(original)
        val result = stringListConverter.fromJsonToStringList(json)

        assertEquals(original, result)
    }

}
