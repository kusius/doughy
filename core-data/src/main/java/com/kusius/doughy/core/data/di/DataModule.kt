package com.kusius.doughy.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.kusius.doughy.core.data.RecipeRepository
import com.kusius.doughy.core.data.DefaultRecipeRepository
import com.kusius.doughy.core.data.sampleBigaRecipe
import com.kusius.doughy.core.data.samplePoolishRecipe
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
    override val activeRecipe: Flow<Recipe>
        get() = TODO("Not yet implemented")

    override val customRecipes: Flow<List<Recipe>> = flowOf(fakeRecipes)
    override val allRecipes: Flow<List<Recipe>>
        get() = TODO("Not yet implemented")

    override suspend fun add(recipe: Recipe): Int {
        throw NotImplementedError()
    }

    override suspend fun selectRecipe(uid: Int) {
        TODO("Not yet implemented")
    }
}

val fakeRecipes = listOf(samplePoolishRecipe, sampleBigaRecipe)
