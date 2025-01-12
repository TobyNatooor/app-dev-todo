package com.example.todo_app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = background,
    onBackground = onBackgroundAndButton,
    primary = checkedBox,
    onPrimary = Color.Black,
    primaryContainer = onBackgroundAndButton,
    onSecondary = buttonIcon,
    tertiaryContainer = appBar,
    surfaceVariant = list,
    onSurfaceVariant = Color.White,
)

private val LightColorScheme = lightColorScheme(
    background = background,
    onBackground = onBackgroundAndButton,
    primary = checkedBox,
    onPrimary = Color.Black,
    primaryContainer = onBackgroundAndButton,
    onSecondary = buttonIcon,
    tertiaryContainer = appBar,
    surfaceVariant = list,
    onSurfaceVariant = Color.White,
)

@Composable
fun TodoappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
