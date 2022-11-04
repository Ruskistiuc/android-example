package com.example.androidexample.presentation

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light",
    group = "Theme",
    showBackground = true,
    backgroundColor = 0xF
)
@Preview(
    name = "Dark",
    group = "Theme",
    showBackground = true,
    backgroundColor = 0x0,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ThemePreview

@ThemePreview
annotation class CombinedPreviews
