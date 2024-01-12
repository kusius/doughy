package com.kusius.doughy.feature.recipe_selection

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.kusius.doughy.core.data.predefinedRecipes
import com.kusius.doughy.core.model.Recipe
import com.kusius.doughy.core.ui.MyApplicationTheme
import com.kusius.doughy.feature.recipe_selection.ui.RecipeSelectionScreen

class RecipeListPreviewParameterProvider : PreviewParameterProvider<List<Recipe>> {
    override val values: Sequence<List<Recipe>>
        get() = sequenceOf(predefinedRecipes)
}