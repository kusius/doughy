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

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.kusius.doughy.core.ui.MyApplicationTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kusius.doughy.core.ui.components.IconWithText
import com.kusius.doughy.feature.recipe.R
import java.lang.Integer.max
import kotlin.math.roundToInt

@Composable
fun RecipeScreen(modifier: Modifier = Modifier, viewModel: RecipeViewModel = hiltViewModel()) {
    RequestNotificationPermissions()
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is RecipeUiState.RecipeData) {
        RecipeScreen(
            recipeData = (items as RecipeUiState.RecipeData),
            onDoughBallsChanged = viewModel::onDoughBallsChanged,
            modifier = modifier
        )
    }
}

@Composable
internal fun RequestNotificationPermissions() {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.i("Doughy", "Notifications granted")
        } else {
            Log.i("Doughy", "Notifications denied")
        }
    }

    val context = LocalContext.current
    when(PackageManager.PERMISSION_DENIED) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) -> {
            SideEffect {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
internal fun RecipeScreen(
    recipeData: RecipeUiState.RecipeData,
    onDoughBallsChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var numDoughBalls by remember { mutableIntStateOf(6) }
    val doughBallWeightGrams = 250
    fun changeDoughBalls(doughBalls: Int) {
        numDoughBalls = doughBalls
        onDoughBallsChanged(numDoughBalls)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.recipe),
                style = MaterialTheme.typography.displaySmall
            )
        }

        IconWithText(
            painter = painterResource(id = R.drawable.outline_assignment_24),
            text = recipeData.recipe.name
        )

        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = recipeData.recipe.description,
            style = MaterialTheme.typography.bodySmall
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            IconWithText(painter = painterResource(id = R.drawable.water_drop), text = "${(recipeData.recipe.percents.hydrationPercent * 100).roundToInt()} %")
            IconWithText(painter = painterResource(id = R.drawable.baseline_timer_24), text = "${recipeData.recipe.rests.bulkRestHours} hrs")
            IconWithText(painter = painterResource(id = R.drawable.baseline_snooze_24), text = "${recipeData.recipe.rests.ballsRestHours} hrs")
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            IconWithText(painter = painterResource(id = R.drawable.hive_24px), text = "$numDoughBalls") // TODO: user provides balls
            Text("X")
            IconWithText(painter = painterResource(id = R.drawable.weight_24px), text = "$doughBallWeightGrams g") // TODO: user provides weight (or default 250g)
            Text("=")
            IconWithText(painter = painterResource(id = R.drawable.weight_24px), text = "${numDoughBalls * doughBallWeightGrams} g")
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.scale_recipe, numDoughBalls),
                style = MaterialTheme.typography.headlineSmall
            )

            IconButton(onClick = { changeDoughBalls(max(numDoughBalls - 1, 1)) }) {
                Icon(
                    Icons.Rounded.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.decrement_dough_balls)
                )
            }
            
            IconButton(onClick = { changeDoughBalls(numDoughBalls + 1) }) {
                Icon(
                    Icons.Rounded.KeyboardArrowUp,
                    contentDescription = stringResource(R.string.increment_dough_balls)
                )
            }
        }

        Divider(modifier = Modifier.padding(24.dp))

        IconWithText(
            painter = painterResource(id = R.drawable.baseline_format_list_bulleted_24),
            text = stringResource(R.string.ingredients)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // preferment ingredients
            Column() {
                 Text(modifier = Modifier.padding(bottom = 8.dp), text = stringResource(R.string.preferment))
                MajorMinorText(
                    majorText = stringResource(R.string.flour),
                    minorText = "${recipeData.prefermentGrams.flour} g"
                )

                MajorMinorText(
                    majorText = stringResource(R.string.water),
                    minorText = "${recipeData.prefermentGrams.water} g"
                )

                if (recipeData.recipe.percents.prefermentUsesYeast) {
                    MajorMinorText(
                        majorText = stringResource(R.string.honey),
                        minorText = "${recipeData.prefermentGrams.honey} g"
                    )

                    MajorMinorText(
                        majorText = stringResource(R.string.fresh_yeast),
                        minorText = "${recipeData.prefermentGrams.yeast} g"
                    )
                }

            }

            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)

            Column {
                Text(modifier = Modifier.padding(bottom = 8.dp), text = stringResource(R.string.dough))

                MajorMinorText(
                    majorText = stringResource(R.string.flour),
                    minorText = "${recipeData.doughGrams.flour} g"
                )

                MajorMinorText(
                    majorText = stringResource(R.string.water),
                    minorText = "${recipeData.doughGrams.water} g"
                )

                MajorMinorText(
                    majorText = stringResource(R.string.salt),
                    minorText = "${recipeData.doughGrams.salt} g"
                )

                Text(text = "+ ${stringResource(id = R.string.preferment)}")

            }
        }
    }
}

@Composable
private fun MajorMinorText(majorText: String, minorText: String, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = majorText)
        Text(text = minorText)
    }
}

// Previews

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES,)
@Composable
private fun DefaultPreview(@PreviewParameter(RecipePreviewParameterProvider::class) recipeData: RecipeUiState.RecipeData) {
    MyApplicationTheme {
        Surface {
            RecipeScreen(recipeData = recipeData, onDoughBallsChanged = {})
        }
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview(@PreviewParameter(RecipePreviewParameterProvider::class) recipeData: RecipeUiState.RecipeData) {
    MyApplicationTheme {
        Surface {
            RecipeScreen(recipeData = recipeData, onDoughBallsChanged = {})
        }
    }
}
