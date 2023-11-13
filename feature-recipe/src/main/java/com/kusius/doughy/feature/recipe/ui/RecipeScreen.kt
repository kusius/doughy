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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.kusius.doughy.core.ui.MyApplicationTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
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
import com.kusius.doughy.core.ui.components.MyDatePickerDialog
import com.kusius.doughy.core.ui.components.MyTimePickerDialog
import com.kusius.doughy.core.ui.components.MajorMinorText
import com.kusius.doughy.feature.recipe.R
import com.kusius.doughy.feature.recipe.ui.components.ScheduleCard
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.lang.Integer.max
import kotlin.math.roundToInt

@Composable
fun RecipeScreen(modifier: Modifier = Modifier, viewModel: RecipeViewModel = hiltViewModel()) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    val schedule by viewModel.scheduleUiState.collectAsStateWithLifecycle()
    val numberOfDoughBalls by viewModel.numberOfDoughBalls.collectAsStateWithLifecycle()
    val doughBallWeightGrams by viewModel.doughBallsWeightGrams.collectAsStateWithLifecycle()

    if (items is RecipeUiState.RecipeData) {
        RecipeScreen(
            recipeData = (items as RecipeUiState.RecipeData),
            scheduleData = schedule,
            numberOfDoughBalls = numberOfDoughBalls,
            doughBallWeightGrams = doughBallWeightGrams,
            onDoughBallsChanged = viewModel::onDoughBallsChanged,
            onScheduleSet = viewModel::shouldBeNotified,
            onScheduleStop = viewModel::stopSchedule,
            modifier = modifier
        )
    }
}

@Composable
internal fun RequestNotificationPermissions(onResponse: (Boolean) -> Unit ) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        onResponse(isGranted)
    }

    val context = LocalContext.current
    when(ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)) {
        PackageManager.PERMISSION_DENIED -> {
            SideEffect {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        PackageManager.PERMISSION_GRANTED -> onResponse(true)
    }
}

@Composable
internal fun RecipeScreen(
    recipeData: RecipeUiState.RecipeData,
    numberOfDoughBalls: Int,
    doughBallWeightGrams: Int,
    scheduleData: ScheduleUiState,
    onDoughBallsChanged: (Int) -> Unit,
    onScheduleSet: (Long, Boolean) -> Unit,
    onScheduleStop: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
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

        OverviewSection(
            recipeData = recipeData,
            numberOfDoughBalls = numberOfDoughBalls,
            doughBallWeightGrams = doughBallWeightGrams,
            onDoughBallsChanged = onDoughBallsChanged
        )

        Divider(modifier = Modifier.padding(24.dp))
        IngredientsSection(recipeData = recipeData)

        Divider(modifier = Modifier.padding(24.dp))
        ScheduleSection(
            scheduleData = scheduleData,
            recipeData = recipeData,
            onScheduleSet = onScheduleSet,
            onScheduleStop = onScheduleStop
        )
    }

}

@Composable
private fun OverviewSection(
    recipeData: RecipeUiState.RecipeData,
    numberOfDoughBalls: Int,
    doughBallWeightGrams: Int,
    onDoughBallsChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var numDoughBalls by remember { mutableIntStateOf(numberOfDoughBalls) }
    val spacing = 16.dp

    fun changeDoughBalls(doughBalls: Int) {
        numDoughBalls = doughBalls
        onDoughBallsChanged(numDoughBalls)
    }

    IconWithText(
        painter = painterResource(id = R.drawable.outline_assignment_24),
        text = recipeData.recipe.name
    )

    Text(
        modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 8.dp),
        text = recipeData.recipe.description,
        style = MaterialTheme.typography.bodySmall
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
            IconWithText(painter = painterResource(id = R.drawable.water_drop), text = "${(recipeData.recipe.percents.hydrationPercent * 100).roundToInt()} %")
            IconWithText(painter = painterResource(id = R.drawable.hive_24px), text = "$numDoughBalls")
        }

        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
            Text("")
            Text("x")
        }

        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
            IconWithText(painter = painterResource(id = R.drawable.baseline_timer_24), text = "${recipeData.recipe.rests.bulkRestHours + recipeData.recipe.rests.prefermentRestHours} hrs")
            IconWithText(painter = painterResource(id = R.drawable.weight_24px), text = "$doughBallWeightGrams g") // TODO: user provides weight (or default 250g)

        }

        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
            Text("")
            Text("=")
        }

        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
            IconWithText(painter = painterResource(id = R.drawable.baseline_snooze_24), text = "${recipeData.recipe.rests.ballsRestHours} hrs")
            IconWithText(painter = painterResource(id = R.drawable.weight_24px), text = "${numDoughBalls * doughBallWeightGrams} g")

        }
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.scale_recipe, numDoughBalls),
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
}

@Composable
private fun IngredientsSection(recipeData: RecipeUiState.RecipeData, modifier: Modifier = Modifier) {

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
@Composable
private fun ScheduleSection(
    scheduleData: ScheduleUiState,
    recipeData: RecipeUiState.RecipeData,
    onScheduleSet: (Long, Boolean) -> Unit,
    onScheduleStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showNotificationPermission by remember { mutableStateOf(false) }

    val now = Clock.System.now()
    val initialSelectedInstant = now.plus(
        recipeData.recipe.rests.totalRestHours(),
        DateTimeUnit.HOUR
    )
    var localDateTime = initialSelectedInstant.toLocalDateTime(TimeZone.currentSystemDefault())

    fun onDateSelected(date: Long?) {
        if (date != null) {
            localDateTime = Instant.fromEpochMilliseconds(date).toLocalDateTime(TimeZone.currentSystemDefault())
            showTimePicker = true
        }
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        localDateTime = LocalDateTime(date = localDateTime.date, time = LocalTime(hour = hour, minute = minute))
        showNotificationPermission = true
    }

    fun onNotificationPermission(isGranted: Boolean) {
        showNotificationPermission = false
        val instantMillis = localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        onScheduleSet(instantMillis, isGranted)
    }

    if (showDatePicker) {
        MyDatePickerDialog(
            initialSelectedDateMillis = initialSelectedInstant.toEpochMilliseconds(),
            onDateSelected = { onDateSelected(it) },
            onDismiss = { showDatePicker = false })
    } else if (showTimePicker) {
        MyTimePickerDialog(
            initialHour = localDateTime.hour,
            initialMinute = localDateTime.minute,
            onTimeSelected = { hour, minute -> onTimeSelected(hour, minute)},
            onDismiss = { showTimePicker = false }
        )
    } else if (showNotificationPermission) {
        RequestNotificationPermissions(onResponse = { onNotificationPermission(it) })
    }

    IconWithText(
        painter = painterResource(id = R.drawable.calendar_month_24px),
        text = stringResource(R.string.schedule)
    )

    if (scheduleData is ScheduleUiState.Inactive) {
        Text(text = stringResource(id = R.string.schedule_description))

        Button(onClick = {showDatePicker = true}
        ) {
            Text(text = stringResource(id = R.string.schedule))
        }
    } else if (scheduleData is ScheduleUiState.ActiveSchedule) {
        scheduleData.steps.forEach { step ->
            ScheduleCard(
                title = stringResource(id = step.title),
                description = stringResource(id = step.description),
                date = step.time
            )
        }
        Button(onClick = onScheduleStop) {
                    Text(text = stringResource(id = R.string.cancel))
                }
    }
}



// Previews

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES,)
@Composable
private fun DefaultPreview(
    @PreviewParameter(RecipePreviewParameterProvider::class) recipeData: RecipeUiState.RecipeData,
    scheduleData: ScheduleUiState = ScheduleUiState.Inactive
) {
    MyApplicationTheme {
        Surface {
            RecipeScreen(
                recipeData = recipeData,
                scheduleData = scheduleData,
                numberOfDoughBalls = 6,
                doughBallWeightGrams = 250,
                onScheduleSet = {_, _ ->},
                onScheduleStop = {},
                onDoughBallsChanged = {}
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview(
    @PreviewParameter(RecipePreviewParameterProvider::class) recipeData: RecipeUiState.RecipeData,
    scheduleData: ScheduleUiState = ScheduleUiState.Inactive
) {
    MyApplicationTheme {
        Surface {
            RecipeScreen(
                recipeData = recipeData,
                scheduleData = scheduleData,
                numberOfDoughBalls = 6,
                doughBallWeightGrams = 250,
                onScheduleSet = {_, _ ->},
                onScheduleStop = {},
                onDoughBallsChanged = {}
            )
        }
    }
}
