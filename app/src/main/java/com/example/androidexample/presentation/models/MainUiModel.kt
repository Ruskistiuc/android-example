package com.example.androidexample.presentation.models

data class MainUiModel(
    val items: List<JokeUiModel>,
    val loading: Boolean,
    val error: Boolean,
    val onClickRetry: () -> Unit,
    val selected: JokeUiModel?,
    val onCloseItemDetails: () -> Unit,
    val onSwipeRefresh: () -> Unit
)
