package com.example.storytellerSampleAndroid.compose.components.items

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ToggleDarkModeContainer(
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Button(modifier = modifier, onClick = {
    onClick()
  }) {
    Text("Toggle UI Theme")
  }
}

