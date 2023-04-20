package com.example.storytellerSampleAndroid.compose.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Color.isDark get(): Boolean = ColorUtils.calculateLuminance(toArgb()) < 0.25

fun Color.Companion.random(): Color {
  return Color(
    red = (0..255).random(),
    green = (0..255).random(),
    blue = (0..255).random()
  )
}
