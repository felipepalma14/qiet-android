package com.motoacademy.android.qiet

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import com.motoacademy.android.qiet.data.local.AppDatabase
import com.motoacademy.android.qiet.data.local.dao.BlockRuleDao
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity

import com.motoacademy.android.qiet.data.local.model.BlockedContact
import com.motoacademy.android.qiet.data.local.model.IntervalTime
import com.motoacademy.android.qiet.data.local.model.DayOfWeek

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
@OptIn(ExperimentalCoroutinesApi::class)
class BlockRuleDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: BlockRuleDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // permitido só em teste
            .build()
        dao = db.blockRuleDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    private fun sampleRule(
        id: Long = 0L,
        name: String = "Regra Teste",
        enabled: Boolean = true
    ) = BlockRuleEntity(
        id = id,
        ruleName = name,
        isEnabled = enabled,
        color = "#FF0000",
        blockedContacts = listOf(BlockedContact("12345", "Contato Teste")),
        blockedRegexRules = listOf(".*spam.*"),
        interval = IntervalTime("09:00", "18:00", listOf(DayOfWeek.MONDAY))
    )

    @Test
    fun insertAndGetById() = runTest {
        val rule = sampleRule(name = "Primeira Regra")
        val id = dao.insert(rule)

        val loaded = dao.getById(id)

        assertNotNull(loaded)
        assertEquals("Primeira Regra", loaded?.ruleName)
    }


    @Test
    fun insert_shouldReplaceOnConflict() = runTest {
        val id = dao.insert(sampleRule(id = 1, name = "Versão 1"))
        dao.insert(sampleRule(id = id, name = "Versão 2"))

        val loaded = dao.getById(id)

        assertEquals("Versão 2", loaded?.ruleName)
    }

    @Test
    fun getAll_shouldReturnInsertedItems() = runTest {
        dao.insert(sampleRule(name = "Regra 1"))
        dao.insert(sampleRule(name = "Regra 2"))

        val all = dao.getAll().first()
        assertEquals(2, all.size)
    }

    @Test
    fun deleteById_shouldRemoveRule() = runTest {
        val id = dao.insert(sampleRule(name = "A ser deletada"))
        assertNotNull(dao.getById(id))

        dao.deleteById(id)
        val loaded = dao.getById(id)

        assertNull(loaded)
    }

    @Test
    fun deleteById_nonExisting_shouldNotCrash() = runTest {
        dao.deleteById(999L)
        // se não lançar exceção, passou
    }

    @Test
    fun updateBlockedContacts_shouldUpdateList() = runTest {
        val id = dao.insert(sampleRule())
        val newContacts = listOf(
            BlockedContact("99999", "Novo Contato"),
            BlockedContact("88888", "Outro Contato")
        )

        dao.updateBlockedContacts(id, newContacts)
        val updated = dao.getById(id)

        assertEquals(2, updated?.blockedContacts?.size)
        assertEquals("Novo Contato", updated?.blockedContacts?.first()?.displayName)
    }

    @Test
    fun updateBlockedRegexRules_shouldUpdateList() = runTest {
        val id = dao.insert(sampleRule())
        val newRegex = listOf(".*promo.*", ".*oferta.*")

        dao.updateBlockedRegexRules(id, newRegex)
        val updated = dao.getById(id)

        assertTrue(updated?.blockedRegexRules?.contains(".*oferta.*") == true)
    }

    @Test
    fun updateInterval_shouldUpdateField() = runTest {
        val id = dao.insert(sampleRule())
        val newInterval = IntervalTime("10:00", "12:00", listOf(DayOfWeek.FRIDAY))

        dao.updateInterval(id, newInterval)
        val updated = dao.getById(id)

        assertEquals("10:00", updated?.interval?.startTime)
        assertEquals(DayOfWeek.FRIDAY, updated?.interval?.daysOfWeek?.first())
    }

    @Test
    fun updateEnabled_shouldToggleValue() = runTest {
        val id = dao.insert(sampleRule(enabled = true))

        dao.updateEnabled(id, false)
        assertFalse(dao.getById(id)?.isEnabled ?: true)

        dao.updateEnabled(id, true)
        assertTrue(dao.getById(id)?.isEnabled ?: false)
    }

}
