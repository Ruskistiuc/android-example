package com.example.androidexample.presentation

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.example.androidexample.presentation.models.ScreenState

@Composable
fun MainScreen(screenState: ScreenState) {
    Surface {
        when (screenState) {
            ScreenState.Loading -> Loading()

            is ScreenState.Error -> Error(screenState.onRetry)

            is ScreenState.Details -> ItemDetailsView(screenState.item, screenState.onClose)

            ScreenState.Empty -> Empty()

            is ScreenState.Loaded -> Content(screenState.items, screenState.onSwipeRefresh)
        }
    }
}
