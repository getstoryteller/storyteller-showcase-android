package com.getstoryteller.storytellershowcaseapp.ui.features.account

import androidx.compose.foundation.background
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getstoryteller.storytellershowcaseapp.ui.components.TabRowDefaults.Divider

@Composable
fun AnalyticsOptionScreen(
  modifier: Modifier = Modifier,
  accountViewModel: AccountViewModel,
) {
  val state = accountViewModel.analyticsUiState.collectAsState().value
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(color = MaterialTheme.colorScheme.surface),
  ) {
    Text(
      text = "TRACKING OPTIONS",
      modifier = modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
      color = MaterialTheme.colorScheme.onSurface,
      fontSize = 12.sp,
      fontWeight = FontWeight.W400,
    )
    Column(
      modifier = Modifier
        .background(color = MaterialTheme.colorScheme.tertiaryContainer),
    ) {
      val options = listOf("Allow personalization", "Allow storyteller tracking", "Allow user activity tracking")
      options.forEachIndexed { index, option ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(option, modifier = Modifier.weight(1f))
          Switch(
            checked = when (index) {
              0 -> state.first
              1 -> state.second
              else -> state.third
            },
            onCheckedChange = { newValue ->
              accountViewModel.updateAnalyticsOption(index, newValue)
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
