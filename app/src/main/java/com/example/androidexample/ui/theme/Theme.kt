package com.example.androidexample.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = Primary_light,
    onPrimary = Color.White,
    primaryVariant = Primary_light,
    secondary = Primary_light,
    onSecondary = Color.White
)

private val DarkColorPalette = darkColors(
    primary = Primary_dark,
    onPrimary = Color.White,
    primaryVariant = Primary_dark,
    secondary = Primary_dark,
    onSecondary = Color.White
)

object AndroidExampleTheme {
    val colors: Colors
        @Composable
        get() = if (isSystemInDarkTheme()) {
            DarkColorPalette
        } else {
            LightColorPalette
        }

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = Shapes

    val paddings: Paddings
        @Composable
        get() = LocalPaddings.current
}

@Composable
fun AndroidExampleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CompositionLocalProvider(
        LocalPaddings provides Paddings()
    ) {
        MaterialTheme(
            colors = colors,
            typography = MaterialTheme.typography,
            shapes = Shapes,
            content = content
        )
    }
}
