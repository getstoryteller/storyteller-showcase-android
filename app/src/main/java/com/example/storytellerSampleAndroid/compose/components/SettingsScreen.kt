package com.example.storytellerSampleAndroid.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen(
  modifier: Modifier = Modifier,
  onBackPressed: () -> Unit,
) {

  Scaffold(
    modifier = modifier,
    topBar = { SettingsTopBar(onBackPressed = onBackPressed) },
    content = { paddingValues ->
      Box(modifier = Modifier.padding(paddingValues)) {
        Text(text = "Settings")
      }
    }
  )
}

@Preview(showBackground = true)
@Composable
fun SettingsTopBar(
  modifier: Modifier = Modifier,
  onBackPressed: () -> Unit = {}
) {
  TopAppBar(
    modifier = modifier,
    navigationIcon = {
      IconButton(
        onClick = onBackPressed
      ) {
        Icon(
          imageVector = Icons.Default.ArrowBack,
          contentDescription = "back"
        )
      }
    },
    title = {}
  )
}