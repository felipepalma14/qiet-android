package com.motoacademy.android.qiet

import org.junit.Test
import org.junit.Assert.*

import com.motoacademy.android.qiet.data.local.model.BlockedContact

class BlockedContactTest {

    @Test
    fun blockedContact_creation_withDisplayName() {
        val contact = BlockedContact("+5511999999999", "João Silva")

        assertEquals("+5511999999999", contact.phoneNumber)
        assertEquals("João Silva", contact.displayName)
    }

    @Test
    fun blockedContact_creation_withoutDisplayName() {
        val contact = BlockedContact("+5511888888888", null)

        assertEquals("+5511888888888", contact.phoneNumber)
        assertNull(contact.displayName)
    }

    @Test
    fun blockedContact_equals_sameValues_returnsTrue() {
        val contact1 = BlockedContact("123", "Test")
        val contact2 = BlockedContact("123", "Test")

        assertEquals(contact1, contact2)
        assertEquals(contact1.hashCode(), contact2.hashCode())
    }

    @Test
    fun blockedContact_equals_differentValues_returnsFalse() {
        val contact1 = BlockedContact("123", "Test")
        val contact2 = BlockedContact("456", "Test")

        assertNotEquals(contact1, contact2)
    }

    @Test
    fun blockedContact_copy_changesSpecifiedFields() {
        val original = BlockedContact("123", "Original")
        val copied = original.copy(phoneNumber = "456", displayName = "Copied")

        assertEquals("456", copied.phoneNumber)
        assertEquals("Copied", copied.displayName)
    }

    @Test
    fun blockedContact_toString_containsRelevantInfo() {
        val contact = BlockedContact("123", "John")
        val string = contact.toString()

        assertTrue(string.contains("123"))
        assertTrue(string.contains("John"))
    }
}