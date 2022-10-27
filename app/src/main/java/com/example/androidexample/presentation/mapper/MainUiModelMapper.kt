package com.example.androidexample.presentation.mapper

import com.example.androidexample.presentation.MainViewModel.State
import com.example.androidexample.presentation.models.JokeUiModel
import com.example.androidexample.presentation.models.MainUiModel
import javax.inject.Inject

class MainUiModelMapper @Inject constructor() {

    fun transform(
        state: State,
        onClickRetry: () -> Unit,
        onClickItem: (JokeUiModel) -> Unit,
        onCloseItemDetails: () -> Unit,
        onSwipeRefresh: () -> Unit
    ): MainUiModel {
        return MainUiModel(
            items = state.data.map { item ->
                JokeUiModel(
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
