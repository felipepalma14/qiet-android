package com.motoacademy.android.qiet

import com.motoacademy.android.qiet.data.local.converter.StringListConverter
import org.junit.Assert.assertEquals
import org.junit.Test

class StringListConverterTest {

    private val converter = StringListConverter()

    @Test
    fun fromStringListToJson_withValidList_isCorrect() {
        val list = listOf("a", "b", "c")
        val json = converter.fromStringListToJson(list)
        assertEquals("""["a","b","c"]""", json)
    }

    @Test
    fun fromStringListToJson_withNullList_returnsEmptyJsonArray() {
        val json = converter.fromStringListToJson(null)
        assertEquals("[]", json)
    }

    @Test
    fun fromJsonToStringList_withValidJson_isCorrect() {
        val json = """["x","y","z"]"""
        val list = converter.fromJsonToStringList(json)
        assertEquals(listOf("x", "y", "z"), list)
    }

    @Test
    fun fromJsonToStringList_withEmptyJsonArray_returnsEmptyList() {
        val json = "[]"
        val list = converter.fromJsonToStringList(json)
        assertEquals(emptyList<String>(), list)
    }
}
