package com.motoacademy.android.qiet

import com.motoacademy.android.qiet.features.create_category.CreateCategoryViewModel


import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateCategoryViewModelTest {

    @Test
    fun initialState_isCorrect() = runTest {
        val viewModel = CreateCategoryViewModel()

        viewModel.uiState.test {
            val initial = awaitItem()

            // CategoryDataState
            assertEquals("", initial.categoryData.categoryName)
            assertEquals("#FFFFFFFF", initial.categoryData.categoryColorHex)
            assertEquals(emptyList<String>(), initial.categoryData.blockedPrefixes)
            assertEquals(emptyList<String>(), initial.categoryData.blockedContacts)

            // TimeRestrictionsState
            assertEquals(false, initial.timeRestrictions.enabled)
            assertEquals("08:00", initial.timeRestrictions.startTime)
            assertEquals("22:00", initial.timeRestrictions.endTime)
            assertEquals(emptySet<String>(), initial.timeRestrictions.weekDays)

            // UiFeedbackState
            assertEquals(false, initial.feedback.isSaving)
            assertEquals(null, initial.feedback.error)

            cancelAndIgnoreRemainingEvents()
        }
    }
}