package com.example.storytellerSampleAndroid.compose.components.items

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NavigatedToApp(
  modifier: Modifier = Modifier,
  text: String,
  onUserNavigatedToApp: (String) -> Unit
) {
  if (text.isEmpty()) return
  Text(modifier = modifier, text = "User navigated to app $text")
}

