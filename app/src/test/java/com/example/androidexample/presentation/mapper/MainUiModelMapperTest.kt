package com.example.androidexample.presentation.mapper

import com.example.androidexample.domain.models.Joke
import com.example.androidexample.presentation.MainViewModel.State
import com.example.androidexample.presentation.models.JokeUiModel
import com.example.androidexample.presentation.models.MainUiModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class MainUiModelMapperTest {

    private val mapper = MainUiModelMapper()

    private val state = State(
        data = emptyList(),
        loading = false,
        error = false,
        selected = null
    )
    private val joke = Joke(joke = "Joke", setup = null, delivery = null)

    @Test
    fun `GIVEN mapper WHEN state is transformed without data THEN should return presentation model`() {
        assertThat(transform(state = state))
            .usingRecursiveComparison()
            .withStrictTypeChecking()
            .ignoringFields(
                MainUiModel::onClickRetry.name,
                MainUiModel::onCloseItemDetails.name,
                MainUiModel::onSwipeRefresh.name,
            )
            .isEqualTo(
                MainUiModel(
                    items = emptyList(),
                    loading = false,
                    error = false,
                    onClickRetry = {},
                    selected = null,
                    onCloseItemDetails = {},
                    onSwipeRefresh = {},
                )
            )
    }

    @Test
    fun `GIVEN mapper WHEN state is transformed with data THEN should return presentation data models`() {
        val mapped = transform(state = state.copy(data = arrayListOf(joke)))

        assertThat(mapped.items)
            .usingRecursiveComparison()
            .withStrictTypeChecking()
            .ignoringFields(JokeUiModel::onClick.name)
            .isEqualTo(
                arrayListOf(JokeUiModel(joke = "Joke", setup = null, delivery = null, onClick = {}))
            )
    }

    @Test
    fun `GIVEN mapper WHEN on click retry is called THEN should call the passed on click retry`() {
        val onClickRetry = mock<() -> Unit>()

        transform(state = state, onClickRetry = onClickRetry).onClickRetry()

        verify(onClickRetry).invoke()
    }

    @Test
    fun `GIVEN mapper WHEN on click item is called THEN should call the passed on click item`() {
        val onClickItem = mock<(JokeUiModel) -> Unit>()
        val clickedItem = mock<JokeUiModel>()

        val mapped = transform(state = state.copy(data = listOf(joke)), onClickItem = onClickItem)
        mapped.items[0].onClick(clickedItem)

        verify(onClickItem).invoke(clickedItem)
    }

    @Test
    fun `GIVEN mapper WHEN on close item details is called THEN should call the passed on close item details`() {
        val onCloseItemDetails = mock<() -> Unit>()

        transform(state = state, onCloseItemDetails = onCloseItemDetails).onCloseItemDetails()

        verify(onCloseItemDetails).invoke()
    }

    @Test
    fun `GIVEN mapper WHEN on swipe refresh is called THEN should call the passed on swipe refresh`() {
        val onSwipeRefresh = mock<() -> Unit>()

        transform(state = state, onSwipeRefresh = onSwipeRefresh).onSwipeRefresh()

        verify(onSwipeRefresh).invoke()
    }

    private fun transform(
        state: State,
        onClickRetry: () -> Unit = {},
        onClickItem: (JokeUiModel) -> Unit = {},
        onCloseItemDetails: () -> Unit = {},
        onSwipeRefresh: () -> Unit = {},
    ): MainUiModel {
        return mapper.transform(
            state = state,
            onClickRetry = onClickRetry,
            onClickItem = onClickItem,
            onCloseItemDetails = onCloseItemDetails,
            onSwipeRefresh = onSwipeRefresh
        )
    }
}
