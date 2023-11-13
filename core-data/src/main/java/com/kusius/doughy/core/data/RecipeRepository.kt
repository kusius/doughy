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

package com.kusius.doughy.core.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.kusius.doughy.core.database.RecipeDao
import com.kusius.doughy.core.database.RecipeEntity
import com.kusius.doughy.core.model.Percents
import com.kusius.doughy.core.model.Recipe
import com.kusius.doughy.core.model.Rests
import com.kusius.doughy.core.model.YeastType
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface RecipeRepository {
    val activeRecipe: Flow<Recipe>
    val recipes: Flow<List<String>>

    suspend fun add(name: String)
}

class DefaultRecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipeRepository {
    override val activeRecipe: Flow<Recipe>
        get() = flowOf(samplePoolishRecipe)

    override val recipes: Flow<List<String>> =
        recipeDao.getRecipes().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        recipeDao.insertRecipe(RecipeEntity(name = name))
    }
}

val samplePoolishRecipe = Recipe(
    name = "Poolish Dough",
    percents = Percents(
        hydrationPercent = 0.65f,
        oilPercent = 0.0f,
        saltPercent = 0.027f,
        sugarsPercent = 0.0034f,
        yeastPercent = 0.0068f,
        yeastType = YeastType.FRESH,
        prefermentPercent = 0.21f,
        prefermentHydrationPercent = 1f,
        prefermentUsesYeast = true,
    ),
    rests = Rests(
        prefermentRestHours = 16,
        bulkRestHours = 16,
        ballsRestHours = 3
    ),
    description = "A simple poolish recipe"
)