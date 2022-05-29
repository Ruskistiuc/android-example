package com.example.androidexample.presentation.models

data class PresentationModel(
    val items: List<PresentationItemModel>,
    val loading: Boolean,
    val error: Boolean,
    val onClickRetry: () -> Unit,
    val selected: PresentationItemModel?,
    val onCloseItemDetails: () -> Unit

)
