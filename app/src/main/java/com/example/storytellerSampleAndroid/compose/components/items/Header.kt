package com.example.storytellerSampleAndroid.compose.components.items

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun Header(text: String) {
  Text(
    text = text,
    modifier = Modifier.padding(24.dp).alpha(.8f),
    style = MaterialTheme.typography.titleMedium,
    color = MaterialTheme.colorScheme.onSurface
  )
}
