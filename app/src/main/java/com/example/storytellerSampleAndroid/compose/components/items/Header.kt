package com.example.storytellerSampleAndroid.compose.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Header(text: String) {
  Text(
    text = text,
    modifier = Modifier.padding(8.dp),
    style = MaterialTheme.typography.titleLarge
  )
}
