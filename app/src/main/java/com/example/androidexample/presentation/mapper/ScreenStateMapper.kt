package com.example.androidexample.presentation.mapper

import com.example.androidexample.domain.models.Joke
import com.example.androidexample.presentation.MainViewModel.InnerState
import com.example.androidexample.presentation.models.ScreenState
import com.example.androidexample.presentation.models.ScreenState.Details.JokeDetailsUiModel
import com.example.androidexample.presentation.models.ScreenState.Loaded.JokeUiModel
import javax.inject.Inject

class ScreenStateMapper @Inject constructor() {

    fun transform(
        state: InnerState,
        onClickRetry: () -> Unit,
        onClickItem: (Joke) -> Unit,
        onCloseItemDetails: () -> Unit,
        onSwipeRefresh: () -> Unit,
    ): ScreenState {
        return when {
            state.loading -> ScreenState.Loading

            state.error -> ScreenState.Error(onClickRetry)

            state.selected != null -> ScreenState.Details(
                item = JokeDetailsUiModel(
                    joke = state.selected.joke,
                    setup = state.selected.setup,
                    delivery = state.selected.delivery,
                ),
                onClose = onCloseItemDetails,
            )

            state.data.isNotEmpty() -> ScreenState.Loaded(
                items = state.data.map { item ->
                    JokeUiModel(
                        joke = item.joke,
                        setup = item.setup,
                        delivery = item.delivery,
                        onClick = { onClickItem(item) },
                    )
                },
                onSwipeRefresh = onSwipeRefresh,
            )

            else -> throw IllegalStateException("Empty state: not implemented")
        }
    }
}
