package com.kusius.doughy.feature.recipe_selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kusius.doughy.core.data.RecipeRepository
import com.kusius.doughy.core.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeSelectionViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    val availableRecipes: StateFlow<List<Recipe>> = recipeRepository.allRecipes.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), listOf()
    )

    fun selectRecipe(recipe: Recipe) = viewModelScope.launch {
        recipeRepository.selectRecipe(recipe.uid)
    }
}