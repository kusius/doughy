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

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.kusius.doughy.core.data.DefaultRecipeRepository
import com.kusius.doughy.core.data.sampleBigaRecipe
import com.kusius.doughy.core.database.RecipeEntity
import com.kusius.doughy.core.database.RecipeDao
import kotlinx.coroutines.flow.flowOf

/**
 * Unit tests for [DefaultRecipeRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultRecipeRepositoryTest {

    @Test
    fun recipes_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultRecipeRepository(FakeRecipeDao(), FakeDatastore())

        repository.add(sampleBigaRecipe)

        assertEquals(1, repository.allRecipes.first().size)
    }

    // TODO: Tests with selection of recipes.

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

    override suspend fun insertRecipe(item: RecipeEntity) {
        data.add(item)
    }
}

private class FakeDatastore: DataStore<Preferences> {
    private val preferences = preferencesOf()
    override val data: Flow<Preferences>
        get() = flowOf(preferences)

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        TODO("Not yet implemented")
    }

}
