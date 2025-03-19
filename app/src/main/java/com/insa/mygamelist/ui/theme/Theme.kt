package com.insa.mygamelist.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = darkPrimary,
    background = darkBackground,
    onBackground = darkText,
    surface = darkSurface,
    onSurface = darkText,
    surfaceVariant = darkSurfaceVariant,
    onSurfaceVariant = darkText
)

private val LightColorScheme = lightColorScheme(
    primary = lightPrimary,
    background = lightBackground,
    onBackground = lightText,
    surface = lightSurface,
    onSurface = lightText,
    surfaceVariant = lightSurfaceVariant,
    )

@Composable
fun MyGamesListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}