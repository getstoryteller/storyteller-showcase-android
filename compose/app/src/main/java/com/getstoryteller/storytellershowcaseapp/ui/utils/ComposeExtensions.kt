package com.getstoryteller.storytellershowcaseapp.ui.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

fun NavController.isCurrentDestination(
  destination: String,
): Boolean {
  return currentDestination?.route?.startsWith(destination) == true
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

@Stable
fun Modifier.debug(): Modifier {
  return this then drawBehind {
    val randomColor = Color(
      red = (0..255).random(),
      green = (0..255).random(),
      blue = (0..255).random(),
    )
    drawRect(randomColor)
  }
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.goingTo(
  destination: String,
) = targetState.destination.route?.startsWith(destination) == true
fun AnimatedContentTransitionScope<NavBackStackEntry>.comingFrom(
  destination: String,
) = initialState.destination.route?.startsWith(destination) == true
