/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kusius.doughy.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.kusius.doughy.core.data.DefaultRecipeRepository
import com.kusius.doughy.core.data.sampleBigaRecipe
import com.kusius.doughy.core.data.samplePoolishRecipe
import com.kusius.doughy.core.database.RecipeEntity
import com.kusius.doughy.core.database.RecipeDao
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope

/**
 * Unit tests for [DefaultRecipeRepository].
 */
class DefaultRecipeRepositoryTest {
    companion object {
        const val TEST_DATASTORE_NAME = "TestPrefsDatastore"
    }

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val testContext: Context = ApplicationProvider.getApplicationContext()

    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) }
        )

    @Test
    fun recipes_newItemSaved_itemIsReturned() = runTest(testDispatcher) {

        val repository = DefaultRecipeRepository(FakeRecipeDao(), testDataStore)

        repository.add(sampleBigaRecipe)

        val recipeList = repository.allRecipes.first()

        assertEquals(1, recipeList.size)
        assertEquals(sampleBigaRecipe.description, recipeList.first().description)
    }

    @Test
    fun recipe_selection_updates() = runTest(testDispatcher) {
        val repository = DefaultRecipeRepository(FakeRecipeDao(), testDataStore)
        // make sure that the selected recipe is in the database
        val uid = repository.add(sampleBigaRecipe)

        repository.selectRecipe(uid)
        val actual = repository.activeRecipe.first()

        assertEquals(sampleBigaRecipe.description, actual.description)
    }

}

private class FakeRecipeDao : RecipeDao {

    private val data = mutableListOf<RecipeEntity>()

    override fun getRecipes(): Flow<List<RecipeEntity>> = flow {
        emit(data)
    }

    override fun getRecipesList(): List<RecipeEntity> {
        return data
    }

    override fun getCustomRecipes(): Flow<List<RecipeEntity>> {
        return flowOf(data.filter { it.isCustom })
    }

    override fun getRecipeByUid(uid: Int): RecipeEntity? {
        return data.find { it.uid == uid }
    }

    override suspend fun insertRecipe(item: RecipeEntity): Long {
        data.add(item)
        return item.uid.toLong()
    }
}
