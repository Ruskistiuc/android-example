package com.example.androidexample.presentation.models

data class JokeUiModel(
    val joke: String?,
    val setup: String?,
    val delivery: String?,
    val onClick: (JokeUiModel) -> Unit
)
