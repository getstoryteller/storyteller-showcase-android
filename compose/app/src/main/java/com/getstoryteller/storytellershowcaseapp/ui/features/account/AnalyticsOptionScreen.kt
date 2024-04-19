package com.getstoryteller.storytellershowcaseapp.ui.features.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.getstoryteller.storytellershowcaseapp.ui.components.TabRowDefaults.Divider
import com.storyteller.Storyteller

@Composable
fun AnalyticsOptionScreen(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.surface),
  ) {
    Column(
      modifier = modifier
        .background(color = MaterialTheme.colorScheme.tertiaryContainer),
    ) {
      val options = listOf("Allow personalization", "Allow storyteller tracking", "Allow user activity tracking")
      val toggles = remember {
        mutableStateListOf(
          Storyteller.eventTrackingOptions.enablePersonalization,
          Storyteller.eventTrackingOptions.enableStorytellerTracking,
          Storyteller.eventTrackingOptions.enableUserActivityTracking,
        )
      }
      options.forEachIndexed { index, option ->
        Row(
          modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp, horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(option, modifier = Modifier.weight(1f))
          Switch(
            checked = toggles[index],
            onCheckedChange = { newValue ->
              toggles[index] = newValue
              Storyteller.eventTrackingOptions = Storyteller.StorytellerEventTrackingOptions(
                enablePersonalization = toggles[0],
                enableStorytellerTracking = toggles[1],
                enableUserActivityTracking = toggles[2],
              )
            },
            colors = SwitchDefaults.colors(
              uncheckedThumbColor = Color.White,
              uncheckedTrackColor = Color.Gray,
            ),
          )
        }
        if (index < options.size - 1) {
          Divider(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .height(1.dp),
          )
        }
      }
    }
  }
}
