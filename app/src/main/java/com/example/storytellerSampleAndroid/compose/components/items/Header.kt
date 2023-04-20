package com.example.storytellerSampleAndroid.compose.components.items

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.storytellerSampleAndroid.compose.theme.isDark

@Composable
fun Header(
  text: String,
  modifier: Modifier = Modifier,
  color: Color = MaterialTheme.colorScheme.onSurface,
) {
  val finalColor = if (color == MaterialTheme.colorScheme.onSurface) {
    MaterialTheme.colorScheme.onSurface
  } else {
    if (color.isDark) Color.White else Color.Black
  }

  Text(
    text = text,
    modifier = modifier
      .padding(24.dp),
    style = MaterialTheme.typography.titleMedium,
    color = finalColor
  )
}
