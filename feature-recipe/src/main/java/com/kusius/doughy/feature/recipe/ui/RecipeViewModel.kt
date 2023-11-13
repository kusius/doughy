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

import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.kusius.doughy.core.data.RecipeRepository
import com.kusius.doughy.core.model.Recipe
import com.kusius.doughy.core.model.Schedule
import com.kusius.doughy.core.model.Type
import com.kusius.doughy.core.notifications.api.NotificationData
import com.kusius.doughy.core.notifications.api.NotificationQueue
import com.kusius.doughy.feature.recipe.R
import com.kusius.doughy.feature.recipe.ui.RecipeUiState.Loading
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject
import kotlin.math.roundToInt

private object PreferencesKeys {
    val PREFERENCES_SCHEDULE_KEY = stringPreferencesKey("recipeScheduleKey")
    val PREFERENCES_DOUGH_BALL_WEIGHT_KEY = intPreferencesKey("settingsDoughBallWeight")
    val PREFERENCES_DOUGH_BALL_AMOUNT_KEY = intPreferencesKey("settingsNumberOfDoughBalls")
}

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val notificationQueue: NotificationQueue,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val _schedule = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Inactive)

    init {
        viewModelScope.launch {
            // initialize the user preferences
            val schedule = dataStore.data.first()[PreferencesKeys.PREFERENCES_SCHEDULE_KEY]
            _schedule.value = if (schedule == null) {
                ScheduleUiState.Inactive
            } else {
                Json.decodeFromString<ScheduleUiState>(schedule)
            }

            val doughBallWeight = dataStore.data.first()[PreferencesKeys.PREFERENCES_DOUGH_BALL_WEIGHT_KEY]
            if (doughBallWeight == null) dataStore.edit { prefs -> prefs[PreferencesKeys.PREFERENCES_DOUGH_BALL_WEIGHT_KEY] = 250 }

            val doughBallAmount = dataStore.data.first()[PreferencesKeys.PREFERENCES_DOUGH_BALL_AMOUNT_KEY]
            if (doughBallAmount == null) dataStore.edit { prefs -> prefs[PreferencesKeys.PREFERENCES_DOUGH_BALL_AMOUNT_KEY] = 8 }

        }
    }

    // gram per dough ball user has selected
    val doughBallsWeightGrams: StateFlow<Int> = dataStore.data.map {
        it[PreferencesKeys.PREFERENCES_DOUGH_BALL_WEIGHT_KEY]
    }.filterNotNull().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 250)

    // total dough balls user has selected to prepare
    val numberOfDoughBalls: StateFlow<Int> = dataStore.data.map {
        it[PreferencesKeys.PREFERENCES_DOUGH_BALL_AMOUNT_KEY]
    }.filterNotNull().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 1)

    // maps repo recipe to presentation state
    val uiState: StateFlow<RecipeUiState> = recipeRepository
        .activeRecipe.combine(numberOfDoughBalls) { recipe, doughBallsAmount ->
            recipe.asUiState(doughBallsAmount * doughBallsWeightGrams.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    // maps preferences for recipe schedule to presentation state
    val scheduleUiState: StateFlow<ScheduleUiState> = _schedule.onEach { scheduleUiState ->
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PREFERENCES_SCHEDULE_KEY] =
                Json.encodeToString(scheduleUiState)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ScheduleUiState.Inactive
    )

    fun addRecipe(name: String) {
        viewModelScope.launch {
            recipeRepository.add(name)
        }
    }

    fun shouldBeNotified(time: Long, userAllowedNotifications: Boolean) {
        viewModelScope.launch {
            val uiState = uiState.value
            if(uiState is RecipeUiState.RecipeData) {
                val steps = CalculateScheduleUseCase(uiState.recipe).invoke(time)
                val stepsUiState = steps.map(Schedule::asUiState)
                _schedule.value = ScheduleUiState.ActiveSchedule(
                    activeStep = 0,
                    earliestCookTime = System.currentTimeMillis() + uiState.recipe.rests.totalRestHours(),
                    steps = stepsUiState
                )

                if (userAllowedNotifications) {
                    notificationQueue.clear()
                    stepsUiState.mapIndexed { index, it ->
                        notificationQueue.add(
                            NotificationData(
                                id = UUID.randomUUID().leastSignificantBits.toInt(),
                                channel = NotificationData.Channel.SCHEDULED,
                                title = it.title,
                                description = it.description,
                                icon = NotificationData.Icon.Res(R.drawable.water_drop),
                                action = null,
                                time = steps[index].time
                            )
                        )
                    }
                }
            }
        }
    }

    fun stopSchedule(): Unit {
        viewModelScope.launch {
            val earliestCookTime = when(val uiState = uiState.value) {
                is RecipeUiState.Error -> 0
                Loading -> 0
                is RecipeUiState.RecipeData -> System.currentTimeMillis() + uiState.recipe.rests.totalRestHours()
            }
            _schedule.value = ScheduleUiState.Inactive
            notificationQueue.clear()
        }
    }

    fun onDoughBallsChanged(amount: Int) = viewModelScope.launch {
        dataStore.edit { it[PreferencesKeys.PREFERENCES_DOUGH_BALL_AMOUNT_KEY] = amount }
    }

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


@Serializable
sealed interface ScheduleUiState {
    @Serializable
    data object Inactive : ScheduleUiState
    @Serializable
    data class ActiveSchedule(
        val activeStep: Int = 0,
        val earliestCookTime: Long,
        val steps: List<ScheduleStep>
    ) : ScheduleUiState
}
@Serializable
data class ScheduleStep(
    @StringRes val title: Int,
    @StringRes val description: Int,
    val time: String
)

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

internal fun Schedule.asUiState(): ScheduleStep {
    val (title, description) = when(type) {
        Type.PREFERMENT -> Pair(R.string.preferment_prep_title, R.string.preferment_prep_description)
        Type.BULK -> Pair(R.string.bulk_prep_title, R.string.bulk_prep_description)
        Type.BALLS -> Pair(R.string.balls_prep_title, R.string.balls_prep_description)
        Type.PREHEAT -> Pair(R.string.preheat_oven_title, R.string.preaheat_oven_description)
        Type.COOK -> Pair(R.string.cook_title, R.string.cook_description)
    }
    val formatDateUseCase = FormatDateUseCase()

    return ScheduleStep(
        title = title,
        description = description,
        time = formatDateUseCase(time)
    )
}