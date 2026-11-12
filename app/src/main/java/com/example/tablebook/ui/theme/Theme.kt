package com.example.tablebook.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val TableBookColorScheme = lightColorScheme(
    primary = TableBookBlue,
    onPrimary = TableBookBackground,
    secondary = TableBookBlueDark,
    background = TableBookBackground,
    surface = TableBookBackground,
    onBackground = TableBookText,
    onSurface = TableBookText
)

@Composable
fun TableBookTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TableBookColorScheme,
        typography = TableBookTypography,
        content = content
    )
}
