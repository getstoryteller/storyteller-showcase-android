package com.getstoryteller.storytellershowcaseapp.ui.utils

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController

fun NavController.isCurrentDestination(
  destination: String,
): Boolean {
  return currentDestination?.route == destination
}

@Stable
fun Modifier.borderTop(
  strokeWidth: Dp,
  color: Color,
) = drawBehind {
  val borderSize = strokeWidth.toPx()
  val y = -borderSize / 2
  drawLine(
    color = color,
    start = Offset(0f, y),
    end = Offset(size.width, y),
    strokeWidth = borderSize,
  )
}
