package com.kusius.doughy.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import com.kusius.doughy.core.data.RecipeRepository
import com.kusius.doughy.core.data.DefaultRecipeRepository
import com.kusius.doughy.core.model.sampleBigaRecipe
import com.kusius.doughy.core.model.samplePoolishRecipe
import com.kusius.doughy.core.model.Recipe
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsRecipeRepository(
        recipeRepository: DefaultRecipeRepository
    ): RecipeRepository
}

class FakeRecipeRepository @Inject constructor() : RecipeRepository {
    // The predefined recipes are templates without a database id, so hand out distinct ones here
    // to make selection behave like the real repository.
    private val recipes = MutableStateFlow(
        fakeRecipes.mapIndexed { index, recipe -> recipe.copy(uid = index + 1) }
    )
    private val selectedUid = MutableStateFlow(1)

    override val activeRecipe: Flow<Recipe> =
        combine(recipes, selectedUid) { all, uid -> all.firstOrNull { it.uid == uid } }
            .filterNotNull()

    override val customRecipes: Flow<List<Recipe>> = recipes.map { it.filter(Recipe::isCustom) }

    override val allRecipes: Flow<List<Recipe>> = recipes

    override suspend fun add(recipe: Recipe): Int {
        val uid = (recipes.value.maxOfOrNull { it.uid } ?: 0) + 1
        recipes.value = recipes.value + recipe.copy(uid = uid)
        return uid
    }

    override suspend fun selectRecipe(uid: Int) {
        selectedUid.value = uid
    }
}

val fakeRecipes = listOf(samplePoolishRecipe, sampleBigaRecipe)
