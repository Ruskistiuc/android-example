package com.example.androidexample.presentation.mapper

import com.example.androidexample.presentation.MainViewModel.State
import com.example.androidexample.presentation.models.PresentationItemModel
import com.example.androidexample.presentation.models.PresentationModel
import javax.inject.Inject

class PresentationModelMapperImpl @Inject constructor() : PresentationModelMapper {

    override fun transform(
        state: State,
        onClickRetry: () -> Unit,
        onClickItem: (PresentationItemModel) -> Unit,
        onCloseItemDetails: () -> Unit,
        onSwipeRefresh: () -> Unit
    ): PresentationModel {
        return PresentationModel(
            items = state.data.map { item ->
                PresentationItemModel(
                    joke = item.joke,
                    setup = item.setup,
                    delivery = item.delivery,
                    onClick = onClickItem
                )
            },
            loading = state.loading,
            error = state.error,
            onClickRetry = onClickRetry,
            selected = state.selected,
            onCloseItemDetails = onCloseItemDetails,
            onSwipeRefresh = onSwipeRefresh
        )
    }
}
