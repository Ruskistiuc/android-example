package com.example.androidexample.presentation.models

data class PresentationItemModel(
    val joke: String?,
    val setup: String?,
    val delivery: String?,
    val onClick: (PresentationItemModel) -> Unit
)
