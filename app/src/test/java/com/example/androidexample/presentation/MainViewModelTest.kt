package com.example.androidexample.presentation

import com.example.androidexample.domain.JokesUseCase
import com.example.androidexample.domain.models.Joke
import com.example.androidexample.presentation.MainViewModel.InnerState
import com.example.androidexample.presentation.mapper.ScreenStateMapper
import com.example.androidexample.presentation.models.ScreenState
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.given
import org.mockito.kotlin.mock

class MainViewModelTest {

    private val useCase = mock<JokesUseCase>()
    private val mapper = mock<ScreenStateMapper>()

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelStream: TestObserver<ScreenState>

    private val stateCaptor = argumentCaptor<InnerState>()
    private val onClickRetryCaptor = argumentCaptor<() -> Unit>()
    private val onClickItemCaptor = argumentCaptor<(Joke) -> Unit>()
    private val onCloseItemDetailsCaptor = argumentCaptor<() -> Unit>()
    private val onSwipeRefreshCaptor = argumentCaptor<() -> Unit>()

    @BeforeEach
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        given(
            mapper.transform(
                state = stateCaptor.capture(),
                onClickRetry = onClickRetryCaptor.capture(),
                onClickItem = onClickItemCaptor.capture(),
                onCloseItemDetails = onCloseItemDetailsCaptor.capture(),
                onSwipeRefresh = onSwipeRefreshCaptor.capture(),
            )
        ).willReturn(mock())
    }

    @AfterEach
    fun teardown() {
        RxAndroidPlugins.reset()
    }

    @Nested
    inner class InitSuccessfully {

        private val joke = Joke(joke = "Joke", setup = null, delivery = null)

        @BeforeEach
        fun setup() {
            given(useCase.getData()).willReturn(Single.just(listOf(joke)))

            viewModel = MainViewModel(useCase, mapper)
            viewModelStream = viewModel.uiModel.test()
        }

        @Test
        fun `GIVEN view model WHEN init successfully THEN should emit loading and items`() {
            assertThat(viewModelStream.values().size).isEqualTo(2)
            assertThat(stateCaptor.allValues[0]).isEqualTo(
                InnerState(data = emptyList(), loading = true, error = false, selected = null)
            )
            assertThat(stateCaptor.allValues[1]).isEqualTo(
                InnerState(data = listOf(joke), loading = false, error = false, selected = null)
            )
        }

        @Test
        fun `GIVEN view model WHEN on swipe refresh successfully THEN should emit state with new data`() {
            val joke2 = Joke(joke = "Joke 2", setup = null, delivery = null)
            given(useCase.getData()).willReturn(Single.just(listOf(joke2)))

            onSwipeRefreshCaptor.allValues[1].invoke()

            assertThat(viewModelStream.values().size).isEqualTo(4)
            assertThat(stateCaptor.allValues[0]).isEqualTo(
                InnerState(data = emptyList(), loading = true, error = false, selected = null)
            )
            assertThat(stateCaptor.allValues[1]).isEqualTo(
                InnerState(data = listOf(joke), loading = false, error = false, selected = null)
            )
            assertThat(stateCaptor.allValues[2]).isEqualTo(
                InnerState(data = emptyList(), loading = true, error = false, selected = null)
            )
            assertThat(stateCaptor.allValues[3]).isEqualTo(
                InnerState(data = listOf(joke2), loading = false, error = false, selected = null)
            )
        }

        @Test
        fun `GIVEN view model WHEN on swipe refresh with error THEN should emit state with error`() {
            given(useCase.getData()).willReturn(Single.error(Exception()))

            onSwipeRefreshCaptor.allValues[1].invoke()

            assertThat(viewModelStream.values().size).isEqualTo(4)
            assertThat(stateCaptor.allValues[0]).isEqualTo(
                InnerState(data = emptyList(), loading = true, error = false, selected = null)
            )
            assertThat(stateCaptor.allValues[1]).isEqualTo(
                InnerState(data = listOf(joke), loading = false, error = false, selected = null)
            )
            assertThat(stateCaptor.allValues[2]).isEqualTo(
                InnerState(data = emptyList(), loading = true, error = false, selected = null)
            )
            assertThat(stateCaptor.allValues[3]).isEqualTo(
                InnerState(data = emptyList(), loading = false, error = true, selected = null)
            )
        }

        @Nested
        inner class OnItemSelected {

            @BeforeEach
            fun setup() {
                onClickItemCaptor.allValues[1].invoke(joke)
            }

            @Test
            fun `GIVEN view model WHEN on click item THEN should emit state with selected`() {
                assertThat(viewModelStream.values().size).isEqualTo(3)
                assertThat(stateCaptor.allValues[2]).isEqualTo(
                    InnerState(data = listOf(joke), loading = false, error = false, selected = joke)
                )
            }

            @Test
            fun `GIVEN view model WHEN on close item details THEN should emit state without selected`() {
                onCloseItemDetailsCaptor.allValues[2].invoke()

                assertThat(viewModelStream.values().size).isEqualTo(4)
                assertThat(stateCaptor.allValues[3]).isEqualTo(
                    InnerState(data = listOf(joke), loading = false, error = false, selected = null)
                )
            }
        }
    }

    @Nested
    inner class InitError {

        @BeforeEach
        fun setup() {
            given(useCase.getData()).willReturn(Single.error(Exception()))

            viewModel = MainViewModel(useCase, mapper)
            viewModelStream = viewModel.uiModel.test()
        }

        @Test
        fun `GIVEN view model WHEN init with error THEN should emit loading and error`() {
            assertThat(viewModelStream.values().size).isEqualTo(2)
            assertThat(stateCaptor.allValues[0]).isEqualTo(
                InnerState(data = emptyList(), loading = true, error = false, selected = null)
            )
            assertThat(stateCaptor.allValues[1]).isEqualTo(
                InnerState(data = emptyList(), loading = false, error = true, selected = null)
            )
        }

        @Test
        fun `GIVEN view model WHEN on click retry with error THEN should emit loading and error`() {
            given(useCase.getData()).willReturn(Single.error(Exception()))

            onClickRetryCaptor.allValues[1].invoke()

            assertThat(viewModelStream.values().size).isEqualTo(4)
            assertThat(stateCaptor.allValues[2]).isEqualTo(
                InnerState(data = emptyList(), loading = true, error = false, selected = null)
            )
            assertThat(stateCaptor.allValues[3]).isEqualTo(
                InnerState(data = emptyList(), loading = false, error = true, selected = null)
            )
        }

        @Test
        fun `GIVEN view model WHEN on click retry successfully THEN should emit loading and items`() {
            val joke = Joke(joke = "Joke", setup = null, delivery = null)
            given(useCase.getData()).willReturn(Single.just(listOf(joke)))

            onClickRetryCaptor.allValues[1].invoke()

            assertThat(viewModelStream.values().size).isEqualTo(4)
            assertThat(stateCaptor.allValues[2]).isEqualTo(
                InnerState(data = emptyList(), loading = true, error = false, selected = null)
            )
            assertThat(stateCaptor.allValues[3]).isEqualTo(
                InnerState(data = listOf(joke), loading = false, error = false, selected = null)
            )
        }
    }
}
