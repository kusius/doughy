package com.kusius.doughy.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.kusius.doughy.core.data.model.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.kusius.doughy.core.database.RecipeDao
import com.kusius.doughy.core.database.RecipeEntity
import com.kusius.doughy.core.database.asRecipe
import com.kusius.doughy.core.model.Recipe
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

interface RecipeRepository {
    val activeRecipe: Flow<Recipe>
    val customRecipes: Flow<List<Recipe>>
    val allRecipes: Flow<List<Recipe>>

    suspend fun add(recipe: Recipe): Int

    suspend fun selectRecipe(uid: Int)
}

private object PreferencesKeys {
    val SELECTED_RECIPE_PREFERENCES_KEY = intPreferencesKey("selectedRecipeIntKey")
}

class DefaultRecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao,
    private val datastore: DataStore<Preferences>
) : RecipeRepository {
    override val activeRecipe: Flow<Recipe>
        get() = datastore.data.mapNotNull { preferences ->
            val uid = preferences[PreferencesKeys.SELECTED_RECIPE_PREFERENCES_KEY]
            if (uid == null) {
                val recipe = recipeDao.getRecipesList().first()
                selectRecipe(recipe.uid)
                null
            } else {
                val recipe = recipeDao.getRecipeByUid(uid)
                recipe?.asRecipe()
            }
        }

    override val customRecipes: Flow<List<Recipe>>
        get() = recipeDao.getCustomRecipes().map { it.map(RecipeEntity::asRecipe) }

    override val allRecipes: Flow<List<Recipe>>
        get() = recipeDao.getRecipes().map { it.map(RecipeEntity::asRecipe)}

    override suspend fun add(recipe: Recipe): Int {
        return recipeDao.insertRecipe(recipe.asEntity()).toInt()
    }

    override suspend fun selectRecipe(uid: Int) {
        datastore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_RECIPE_PREFERENCES_KEY] = uid
        }
    }
}