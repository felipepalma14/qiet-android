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

import com.motoacademy.android.qiet.data.local.AppDatabase
import com.motoacademy.android.qiet.data.local.dao.BlockRuleDao
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity

@RunWith(RobolectricTestRunner::class)
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

    @Test
    fun insertAndGetById() = runTest {
        val rule = BlockRuleEntity(
            ruleName = "Regra Teste",
            isEnabled = true,
            color = "#FF0000"
        )

        val id = dao.insert(rule)
        val loaded = dao.getById(id)

        assertNotNull(loaded)
        assertEquals("Regra Teste", loaded?.ruleName)
        assertTrue(loaded?.isEnabled == true)
    }

    @Test
    fun updateEnabled_shouldChangeValue() = runTest {
        val id = dao.insert(
            BlockRuleEntity(
                ruleName = "Desabilitar depois",
                isEnabled = true,
                color = "#00FF00"
            )
        )

        dao.updateEnabled(id, false)

        val updated = dao.getById(id)
        assertFalse(updated?.isEnabled ?: true)
    }

    @Test
    fun getAll_shouldReturnInsertedItems() = runTest {
        dao.insert(BlockRuleEntity(ruleName = "Regra 1", isEnabled = true, color = "#0000FF"))
        dao.insert(BlockRuleEntity(ruleName = "Regra 2", isEnabled = false, color = "#FFFFFF"))

        val all = dao.getAll().first()
        assertEquals(2, all.size)
    }
}
