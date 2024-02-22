package com.example.androidexample.presentation.models

sealed interface ScreenState {

    data object Loading : ScreenState

    data class Error(val onRetry: () -> Unit) : ScreenState

    data object Empty : ScreenState

    data class Loaded(
        val items: List<JokeUiModel>,
        val onSwipeRefresh: () -> Unit,
    ) : ScreenState {

        data class JokeUiModel(
            val joke: String?,
            val setup: String?,
            val delivery: String?,
            val onClick: () -> Unit,
        )
    }

    data class Details(
        val item: JokeDetailsUiModel,
        val onClose: () -> Unit,
    ) : ScreenState {

        data class JokeDetailsUiModel(
            val joke: String?,
            val setup: String?,
            val delivery: String?,
        )
    }
}
