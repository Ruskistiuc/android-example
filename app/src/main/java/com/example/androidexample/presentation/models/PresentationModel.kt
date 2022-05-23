package com.example.androidexample.presentation.models

data class PresentationModel(
    val items: List<PresentationItemModel>,
    val isLoading: Boolean,
    val isError: Boolean
)
