package com.example.storytellerSampleAndroid.compose.components.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Banner(color: Color) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp)
      .height(300.dp)
      .background(color = color)
  ) {
    Header(modifier = Modifier.align(Alignment.Center), text = "Banner", color = color)
  }
}
