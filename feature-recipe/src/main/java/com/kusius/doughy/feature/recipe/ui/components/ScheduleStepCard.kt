package com.kusius.doughy.feature.recipe.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kusius.doughy.core.ui.DoughyTheme

@Composable
fun ScheduleCard(
    title: String,
    description: String,
    date: String,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(elevation = CardDefaults.cardElevation(10.dp)) {
        Column(
            modifier =
                Modifier
                    .padding(8.dp)
                    .animateContentSize(
                        animationSpec =
                            spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium,
                            ),
                    ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = date)
            }

            Text(text = description, textAlign = TextAlign.Justify)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewScheduleCard() {
    DoughyTheme {
        Surface {
            ScheduleCard(
                title = "Time to cook!",
                description = "These are some instructions on cooking your pizza",
                date = "13 Oct 15:30",
            )
        }
    }
}
