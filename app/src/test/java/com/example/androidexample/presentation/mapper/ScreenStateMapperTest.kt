package com.example.androidexample.presentation.mapper

import com.example.androidexample.domain.models.Joke
import com.example.androidexample.presentation.MainViewModel.InnerState
import com.example.androidexample.presentation.models.ScreenState
import com.example.androidexample.presentation.models.ScreenState.Loaded.JokeUiModel
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ScreenStateMapperTest {

    private val mapper = ScreenStateMapper()

    private val state = InnerState(
        data = emptyList(),
        loading = false,
        error = false,
        selected = null,
    )
    private val joke = Joke(joke = "Joke", setup = null, delivery = null)

    @Test
    fun `GIVEN mapper WHEN state is loading THEN should return loading screen state`() {
        assertThat(transform(state.copy(loading = true))).isEqualTo(ScreenState.Loading)
    }

    @Nested
    inner class OnError {

        private val onClickRetry = mock<() -> Unit>()
        private lateinit var mapped: ScreenState

        @BeforeEach
        fun setup() {
            mapped = transform(state = state.copy(error = true), onClickRetry = onClickRetry)
        }

        @Test
        fun `GIVEN mapper WHEN state is on error THEN should return error screen state`() {
            assertThat(mapped).isEqualTo(ScreenState.Error(onClickRetry))
        }

        @Test
        fun `GIVEN mapper WHEN on retry is called THEN should call the passed on click retry`() {
            (mapped as ScreenState.Error).onRetry()

            verify(onClickRetry).invoke()
        }
    }

    @Nested
    inner class OnItemSelected {

        private val onCloseItemDetails = mock<() -> Unit>()
        private lateinit var mapped: ScreenState

        @BeforeEach
        fun setup() {
            mapped = transform(
                state = state.copy(selected = joke),
                onCloseItemDetails = onCloseItemDetails,
            )
        }

        @Test
        fun `GIVEN mapper WHEN state has selected item THEN should return details screen state`() {
            assertThat(mapped).isEqualTo(
                ScreenState.Details(
                    item = ScreenState.Details.JokeDetailsUiModel(
                        joke = "Joke",
                        setup = null,
                        delivery = null,
                    ),
                    onClose = onCloseItemDetails,
                )
            )
        }

        @Test
        fun `GIVEN mapper WHEN on close is called THEN should call the passed on close item details`() {
            (mapped as ScreenState.Details).onClose()

            verify(onCloseItemDetails).invoke()
        }
    }

    @Nested
    inner class OnLoaded {

        private val onClickItem = mock<(Joke) -> Unit>()
        private val onSwipeRefresh = mock<() -> Unit>()
        private lateinit var mapped: ScreenState

        @BeforeEach
        fun setup() {
            mapped = transform(
                state = state.copy(data = arrayListOf(joke)),
                onClickItem = onClickItem,
                onSwipeRefresh = onSwipeRefresh,
            )
        }

        @Test
        fun `GIVEN mapper WHEN state has data THEN should return loaded screen state`() {
            assertThat((mapped as ScreenState.Loaded).items)
                .usingRecursiveComparison()
                .withStrictTypeChecking()
                .ignoringFields(JokeUiModel::onClick.name)
                .isEqualTo(
                    arrayListOf(
                        JokeUiModel(
                            joke = "Joke",
                            setup = null,
                            delivery = null,
                            onClick = {},
                        )
                    )
                )
        }

        @Test
        fun `GIVEN mapper WHEN on click item is called THEN should call the passed on click item with item`() {
            (mapped as ScreenState.Loaded).items[0].onClick()

            verify(onClickItem).invoke(joke)
        }

        @Test
        fun `GIVEN mapper WHEN on swipe refresh is called THEN should call the passed on swipe refresh`() {
            (mapped as ScreenState.Loaded).onSwipeRefresh()

            verify(onSwipeRefresh).invoke()
        }
    }

    @Test
    fun `GIVEN mapper WHEN state has no data THEN should throw an exception`() {
        assertThatThrownBy { transform(state) }.isInstanceOf(IllegalStateException::class.java)
    }

    private fun transform(
        state: InnerState,
        onClickRetry: () -> Unit = {},
        onClickItem: (Joke) -> Unit = {},
        onCloseItemDetails: () -> Unit = {},
        onSwipeRefresh: () -> Unit = {},
    ): ScreenState {
        return mapper.transform(
            state = state,
            onClickRetry = onClickRetry,
            onClickItem = onClickItem,
            onCloseItemDetails = onCloseItemDetails,
            onSwipeRefresh = onSwipeRefresh,
        )
    }
}
