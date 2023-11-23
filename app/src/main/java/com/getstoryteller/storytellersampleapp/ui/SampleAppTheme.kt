package com.getstoryteller.storytellersampleapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = Color(0xFF1C62EB),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF1C62EB),
    background = Color(0xFF000000),
    surface = Color(0xFF0D0D0D),
    primaryVariant = Color(0xFF1A1A1A),
    onSurface = Color(0xB3FFFFFF),
    onBackground = Color(0xFFFFFFFF),

)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = Color(0xFF1C62EB),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF1C62EB),
    surface = Color(0xFFF5F5F5),
    background = Color(0xFFFFFFFF),
    primaryVariant = Color(0xFFFFFFFF),
    onSurface = Color(0xB31A1A1A),
    onBackground = Color(0xFF1A1A1A)
)

@Composable
fun SampleAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
        content = content,
        colors = colors
    )
}