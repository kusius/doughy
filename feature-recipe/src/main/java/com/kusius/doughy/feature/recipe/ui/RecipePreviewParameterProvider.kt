package com.kusius.doughy.feature.recipe.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.kusius.doughy.core.data.samplePoolishRecipe

class RecipePreviewParameterProvider : PreviewParameterProvider<RecipeUiState> {
    override val values: Sequence<RecipeUiState> = sequenceOf(
            samplePoolishRecipe.asUiState(1500)
    )
}

class SchedulePreviewParameterProvider : PreviewParameterProvider<ScheduleUiState> {
    override val values: Sequence<ScheduleUiState> = sequenceOf(
        ScheduleUiState.Inactive
    )
}