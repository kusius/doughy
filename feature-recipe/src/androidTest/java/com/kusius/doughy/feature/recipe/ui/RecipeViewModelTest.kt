package com.kusius.doughy.feature.recipe.ui

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class RecipeViewModelTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var viewModel: RecipeViewModel

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun uiState_initiallyLoading() = runTest {
        assertEquals(viewModel.uiState.first(), RecipeUiState.Loading)
    }

    @Test
    fun uiState_onItemSaved_isDisplayed() = runTest {
        assertEquals(viewModel.uiState.first(), RecipeUiState.Loading)
    }
}