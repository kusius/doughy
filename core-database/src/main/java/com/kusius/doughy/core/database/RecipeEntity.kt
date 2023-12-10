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

package com.kusius.doughy.core.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.kusius.doughy.core.model.Percents
import com.kusius.doughy.core.model.Recipe
import com.kusius.doughy.core.model.Rests
import com.kusius.doughy.core.model.YeastType
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "recipe")
data class RecipeEntity(
    val hydrationPercent: Float,
    val name: String,
    val oilPercent: Float,
    val saltPercent: Float,
    val sugarsPercent: Float,
    val yeastPercent: Float,
    val yeastType: YeastType,
    val prefermentPercent: Float,
    val prefermentHydrationPercent: Float,
    val prefermentUsesYeast: Boolean,
    val prefermentRestHours: Int,
    val bulkRestHours: Int,
    val ballsRestHours: Int,
    val description: String,
    val isCustom: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

fun RecipeEntity.asRecipe() = Recipe(
    name = name,
    percents = Percents(
        hydrationPercent = hydrationPercent,
        oilPercent = oilPercent,
        saltPercent = saltPercent,
        sugarsPercent = sugarsPercent,
        yeastPercent = yeastPercent,
        yeastType = yeastType,
        prefermentHydrationPercent = prefermentHydrationPercent,
        prefermentPercent = prefermentPercent,
        prefermentUsesYeast = prefermentUsesYeast
    ),
    rests = Rests(
        prefermentRestHours = prefermentRestHours,
        bulkRestHours = bulkRestHours,
        ballsRestHours = ballsRestHours,
    ),
    description = description,
    isCustom = isCustom,
    uid = uid
)

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe ORDER BY name DESC")
    fun getRecipes(): Flow<List<RecipeEntity>>
    @Query("SELECT * FROM recipe ORDER BY name DESC")
    fun getRecipesList(): List<RecipeEntity>

    @Query("SELECT * FROM recipe WHERE isCustom = 1")
    fun getCustomRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipe WHERE uid = :uid")
    fun getRecipeByUid(uid: Int): RecipeEntity?

    @Insert
    suspend fun insertRecipe(item: RecipeEntity): Long
}
