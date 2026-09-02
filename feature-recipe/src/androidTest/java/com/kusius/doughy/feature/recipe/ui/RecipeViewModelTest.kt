package com.kusius.doughy.feature.recipe.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kusius.doughy.core.data.RecipeRepository
import com.kusius.doughy.core.notifications.api.NotificationQueue
import dagger.hilt.android.qualifiers.ApplicationContext
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

    // Hilt refuses to inject an @HiltViewModel directly, so the view model is built here
    // from its injected dependencies.
    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Inject
    lateinit var notificationQueue: NotificationQueue

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    @Inject
    @ApplicationContext
    lateinit var context: Context

    private lateinit var viewModel: RecipeViewModel

    @Before
    fun init() {
        hiltRule.inject()
        viewModel = RecipeViewModel(recipeRepository, notificationQueue, dataStore, context)
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