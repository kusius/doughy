package com.kusius.doughy.feature.recipe_selection.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kusius.doughy.core.model.Recipe
import com.kusius.doughy.core.ui.MyApplicationTheme
import com.kusius.doughy.feature.recipe_selection.R
import com.kusius.doughy.feature.recipe_selection.RecipeListPreviewParameterProvider
import com.kusius.doughy.feature.recipe_selection.RecipeSelectionViewModel

@Composable
fun RecipeSelectionScreen(modifier: Modifier = Modifier, viewModel: RecipeSelectionViewModel = hiltViewModel()) {
    val recipes by viewModel.availableRecipes.collectAsStateWithLifecycle()

    RecipeSelectionScreen(recipes = recipes, modifier = modifier)
}

@Composable
internal fun RecipeSelectionScreen(recipes: List<Recipe>, modifier: Modifier = Modifier) {
    LazyColumn(verticalArrangement = Arrangement.SpaceBetween){
        items(recipes) { recipe ->
            Row(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(recipe.name)
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.open_recipe_details)
                )
            }
            Divider()
        }
    }
}



@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DefaultPreview(
    @PreviewParameter(RecipeListPreviewParameterProvider::class) recipes: List<Recipe>
) {
    MyApplicationTheme {
        Surface {
            RecipeSelectionScreen(recipes)
        }
    }
}


