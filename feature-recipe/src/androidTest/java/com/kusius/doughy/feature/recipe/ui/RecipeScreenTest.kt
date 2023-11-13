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

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kusius.doughy.core.model.Percents
import com.kusius.doughy.core.model.Recipe
import com.kusius.doughy.core.model.Rests
import com.kusius.doughy.core.model.YeastType
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for [RecipeScreen].
 */
@RunWith(AndroidJUnit4::class)
class RecipeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            RecipeScreen(
                recipeData = FAKE_DATA,
                scheduleData = ScheduleUiState.Inactive,
                onDoughBallsChanged = {},
                onScheduleStop = {},
                onScheduleSet = {_, _ ->})
        }
    }
    @Test
    fun firstItem_exists() {
        composeTestRule.onNodeWithText(FAKE_DATA.recipe.name).assertExists().performClick()
    }
}

private val FAKE_DATA = RecipeUiState.RecipeData(
    totalFlourGrams = 1000,
    recipe = Recipe(
        name = "Poolish Dough",
        percents = Percents(
            hydrationPercent = 60f,
            oilPercent = 2.5f,
            saltPercent = 3f,
            sugarsPercent = 0f,
            yeastPercent = 1f,
            yeastType = YeastType.FRESH,
            prefermentPercent = 30f,
            prefermentHydrationPercent = 100f,
            prefermentUsesYeast = true,
        ),
        rests = Rests(
            prefermentRestHours = 16,
            bulkRestHours = 16,
            ballsRestHours = 6,
        ),
        description = "A simple poolish recipe"
    ),
    prefermentGrams = PrefermentGrams(
        flour = "300",
        water = "300",
        honey = "5",
        yeast = "3"
    ),
    doughGrams = DoughGrams(
        flour = "300",
        water = "300",
        oil = "25",
        salt = "15"
    ),
)


