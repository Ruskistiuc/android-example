package com.example.androidexample.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Paddings(
    val padding_xs: Dp = 4.dp,
    val padding_s: Dp = 8.dp,
    val padding_m: Dp = 12.dp,
    val padding_l: Dp = 16.dp,
    val padding_xl: Dp = 24.dp
)

internal val LocalPaddings = staticCompositionLocalOf { Paddings() }
