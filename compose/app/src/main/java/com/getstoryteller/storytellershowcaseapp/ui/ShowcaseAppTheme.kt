package com.getstoryteller.storytellershowcaseapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class StorytellerColors(
  val background: Color = Color.Unspecified,
  val success: Color = Color.Unspecified,
  val subtitle: Color = Color.Unspecified,
  val header: Color = Color.Unspecified,
  val border: Color = Color.Unspecified,
  val linkIconColor: Color = Color.Unspecified,
)

val LocalStorytellerColorsPalette = staticCompositionLocalOf { StorytellerColors() }

private val StorytellerLightColorScheme =
  StorytellerColors(
    background = Color(0xFFE5E5E5),
    success = Color(0xFF57F35D),
    subtitle = Color(0x993C3C43),
    header = Color(0xFF000000),
    border = Color(0x5C3C3C43),
    linkIconColor = Color(0xFFC5C5C5),
  )

private val StorytellerDarkColorScheme =
  StorytellerColors(
    background = Color(0xFF1A1A1A),
    success = Color(0xFF57F35D),
    subtitle = Color(0x9997979D),
    header = Color(0xFFFFFFFF),
    border = Color(0xD9545458),
    linkIconColor = Color(0xFFC5C5C5),
  )

@SuppressLint("ConflictingOnColor")
private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFF1C62EB),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF1C62EB),
    background = Color(0xFF000000),
    surface = Color(0xFF0D0D0D),
    surfaceVariant = Color(0xFF555555),
    onSurface = Color(0xB3FFFFFF),
    onBackground = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF1A1A1A),
    error = Color(0xFFE21219),
  )

@SuppressLint("ConflictingOnColor")
private val LightColorScheme =
  lightColorScheme(
    primary = Color(0xFF1C62EB),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF1C62EB),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFFEFEFF0),
    onSurface = Color(0xB31A1A1A),
    onBackground = Color(0xFF1A1A1A),
    tertiaryContainer = Color(0xFFFFFFFF),
    error = Color(0xFFE21219),
  )

@Composable
fun ShowcaseAppTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val materialColorScheme =
    if (darkTheme) {
      DarkColorScheme
    } else {
      LightColorScheme
    }

  val storytellerColorsPalette =
    if (darkTheme) {
      StorytellerDarkColorScheme
    } else {
      StorytellerLightColorScheme
    }

  CompositionLocalProvider(LocalStorytellerColorsPalette provides storytellerColorsPalette) {
    MaterialTheme(
      content = content,
      colorScheme = materialColorScheme,
    )
  }
}
