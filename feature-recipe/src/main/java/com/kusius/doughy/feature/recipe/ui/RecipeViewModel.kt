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

package com.kusius.doughy.feature.recipe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.kusius.doughy.core.data.RecipeRepository
import com.kusius.doughy.core.model.Recipe
import com.kusius.doughy.feature.recipe.ui.RecipeUiState.Loading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    private val _doughBallWeightGrams = 250
    private val _doughBallsAmount = MutableStateFlow(6)

    // maps repo recipe to presentation state
    val uiState: StateFlow<RecipeUiState> = recipeRepository
        .activeRecipe.combine(_doughBallsAmount) { recipe, doughBallsAmount ->
            recipe.asUiState(doughBallsAmount * _doughBallWeightGrams)
        }
//        .map<Recipe, RecipeUiState> { RecipeUiState.RecipeData(recipe = it) }
//        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addRecipe(name: String) {
        viewModelScope.launch {
            recipeRepository.add(name)
        }
    }

    fun onDoughBallsChanged(amount: Int) =
        viewModelScope.launch { _doughBallsAmount.value = amount }

}


data class PrefermentGrams(
    val flour: String,
    val water: String,
    val honey: String,
    val yeast: String
)

data class DoughGrams(
    val flour: String,
    val water: String,
    val oil: String,
    val salt: String
)

sealed interface RecipeUiState {
    data object Loading : RecipeUiState
    data class Error(val throwable: Throwable) : RecipeUiState
    data class RecipeData(
        val totalFlourGrams: Int,
        val recipe: Recipe,
        val prefermentGrams: PrefermentGrams,
        val doughGrams: DoughGrams
    ) : RecipeUiState
}

internal fun Recipe.asUiState(totalDoughGrams: Int): RecipeUiState.RecipeData {
    val totalFlourGrams = with(percents) {
        totalDoughGrams / (1 + oilPercent + yeastPercent + sugarsPercent + saltPercent + hydrationPercent)
    }.roundToInt()

    val grams = CalculateGramsUseCase(this.percents).invoke(totalFlourGrams)

    val prefermentGrams = PrefermentGrams(
        flour = grams.pFlour.toString(),
        water = grams.pWater.toString(),
        honey = grams.pSugars.formatForDisplay(),
        yeast = grams.pYeast.formatForDisplay(),
    )

    val doughGrams = DoughGrams(
        flour = grams.flour.toString(),
        water = grams.water.toString(),
        oil = grams.oil.formatForDisplay(),
        salt = grams.salt.formatForDisplay(),
    )

    return RecipeUiState.RecipeData(
        totalFlourGrams = totalFlourGrams,
        prefermentGrams = prefermentGrams,
        doughGrams = doughGrams,
        recipe = this
    )
}

/*
How to calculate total flour (and therefore whole other recipe), only from the
total dough weight (dough balls * dough ball weight)

62.38% * x + 100% * x = 1500


1.6238 * x = 1500 => x = 1500/1.6238


Flour = Total Dough Weight / (1 + Sum of percentages other than flour)
 */