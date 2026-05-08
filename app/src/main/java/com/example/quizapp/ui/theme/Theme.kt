package com.example.quizapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = QuizBlue,
    secondary = QuizOrange,
    tertiary = QuizGreen,

    background = QuizBackground,
    surface = QuizCard,

    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onTertiary = androidx.compose.ui.graphics.Color.White,

    onBackground = QuizTextDark,
    onSurface = QuizTextDark
)

private val DarkColorScheme = darkColorScheme(
    primary = QuizBlue,
    secondary = QuizOrange,
    tertiary = QuizGreen
)

@Composable
fun QuizAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme =
        if (darkTheme) DarkColorScheme
        else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}