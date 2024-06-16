
package com.example.geminichat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Définir les couleurs personnalisées
val DarckBleu = Color(0xFF0B0530)
val YelloOrange = Color(0xFFF5A45C)
val BleuLight = Color(0xFF109FF8)
val PurpleGray = Color(0xFF7E73A0)

// Définir le schéma de couleurs pour le mode clair
private val LightColorScheme = ColorScheme(
    primary = DarckBleu,
    onPrimary = Color.White,
    primaryContainer = DarckBleu,
    onPrimaryContainer = Color.White,
    secondary = YelloOrange,
    onSecondary = Color.Black,
    secondaryContainer = YelloOrange,
    onSecondaryContainer = Color.Black,
    tertiary = BleuLight,
    onTertiary = Color.White,
    tertiaryContainer = BleuLight,
    onTertiaryContainer = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = PurpleGray,
    onSurfaceVariant = Color.Black,
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color.Red,
    onErrorContainer = Color.White,
    outline = PurpleGray,
    outlineVariant = Color.LightGray,
    inverseOnSurface = Color.White,
    inverseSurface = Color.Black,
    inversePrimary = BleuLight,
    surfaceTint = DarckBleu,
    scrim = Color.Black
)

// Définir le schéma de couleurs pour le mode sombre
private val DarkColorScheme = ColorScheme(
    primary = DarckBleu,
    onPrimary = Color.White,
    primaryContainer = DarckBleu,
    onPrimaryContainer = Color.White,
    secondary = YelloOrange,
    onSecondary = Color.Black,
    secondaryContainer = YelloOrange,
    onSecondaryContainer = Color.Black,
    tertiary = BleuLight,
    onTertiary = Color.White,
    tertiaryContainer = BleuLight,
    onTertiaryContainer = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceVariant = PurpleGray,
    onSurfaceVariant = Color.White,
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color.Red,
    onErrorContainer = Color.White,
    outline = PurpleGray,
    outlineVariant = Color.DarkGray,
    inverseOnSurface = Color.Black,
    inverseSurface = Color.White,
    inversePrimary = BleuLight,
    surfaceTint = DarckBleu,
    scrim = Color.White
)

@Composable
fun GeminiChatTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme,
        content = content
    )
}