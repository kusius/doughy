package com.kusius.doughy.feature.recipe_selection

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.kusius.doughy.core.model.predefinedRecipes
import com.kusius.doughy.core.model.Recipe

class RecipeListPreviewParameterProvider : PreviewParameterProvider<List<Recipe>> {
    override val values: Sequence<List<Recipe>>
        get() = sequenceOf(predefinedRecipes)
}
