package com.example.androidexample.presentation.mapper

import com.example.androidexample.presentation.MainViewModel.State
import com.example.androidexample.presentation.models.PresentationItemModel
import com.example.androidexample.presentation.models.PresentationModel

interface PresentationModelMapper {

    fun transform(
        state: State,
        onClickRetry: () -> Unit,
        onClickItem: (PresentationItemModel) -> Unit,
        onCloseItemDetails: () -> Unit
    ): PresentationModel
}
