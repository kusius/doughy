package com.kusius.doughy.feature.recipe.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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
import com.kusius.doughy.core.ui.MyApplicationTheme

@Composable
fun ScheduleCard(title: String, description: String, date: String, modifier: Modifier = Modifier) {
    ElevatedCard(elevation = CardDefaults.cardElevation(10.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, style = MaterialTheme.typography.labelLarge)
                Text(text = date, style = MaterialTheme.typography.labelLarge)
            }

            Text(text = description, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Justify)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewScheduleCard() {
    MyApplicationTheme {
        Surface {
            ScheduleCard(
                title = "Cook time!",
                description = "Place your topped pizza in your oven. First with only the " +
                        "sauce. After its initial rise, take it out and place your other toppings. Cook another 1 minute until done.",
                date = "13 Oct 15:30"
            )
        }
    }
}