package com.example.androidexample.presentation

import com.example.androidexample.domain.JokesUseCase
import com.example.androidexample.domain.models.Joke
import com.example.androidexample.presentation.MainViewModel.State
import com.example.androidexample.presentation.mapper.MainUiModelMapper
import com.example.androidexample.presentation.models.JokeUiModel
import com.example.androidexample.presentation.models.MainUiModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.given
import org.mockito.kotlin.mock

class MainViewModelTest {

    private val useCase = mock<JokesUseCase>()
    private val mapper = mock<MainUiModelMapper>()

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelStream: TestObserver<MainUiModel>

    private val stateCaptor = argumentCaptor<State>()
    private val onClickRetryCaptor = argumentCaptor<() -> Unit>()
    private val onClickItemCaptor = argumentCaptor<(JokeUiModel) -> Unit>()
    private val onCloseItemDetailsCaptor = argumentCaptor<() -> Unit>()
    private val onSwipeRefreshCaptor = argumentCaptor<() -> Unit>()

    @Before
    fun setup() {
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

    @Test
    fun `GIVEN view model WHEN init successfully THEN should emit loading and items`() {
        val joke = Joke(joke = "Joke", setup = null, delivery = null)
        given(useCase.getData()).willReturn(Single.just(listOf(joke)))

        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()

        assertThat(viewModelStream.values().size).isEqualTo(2)
        assertThat(stateCaptor.allValues[0]).isEqualTo(
            State(data = emptyList(), loading = true, error = false, selected = null)
        )
        assertThat(stateCaptor.allValues[1]).isEqualTo(
            State(data = listOf(joke), loading = false, error = false, selected = null)
        )
    }

    @Test
    fun `GIVEN view model WHEN init with error THEN should emit loading and error`() {
        given(useCase.getData()).willReturn(Single.error(Exception()))

        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()

        assertThat(viewModelStream.values().size).isEqualTo(2)
        assertThat(stateCaptor.allValues[0]).isEqualTo(
            State(data = emptyList(), loading = true, error = false, selected = null)
        )
        assertThat(stateCaptor.allValues[1]).isEqualTo(
            State(data = emptyList(), loading = false, error = true, selected = null)
        )
    }

    @Test
    fun `GIVEN view model WHEN on click retry with error THEN should emit loading and error`() {
        given(useCase.getData()).willReturn(Single.error(Exception()))

        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()
        given(useCase.getData()).willReturn(Single.error(Exception()))
        onClickRetryCaptor.allValues[1].invoke()

        assertThat(viewModelStream.values().size).isEqualTo(4)
        assertThat(stateCaptor.allValues[2]).isEqualTo(
            State(data = emptyList(), loading = true, error = false, selected = null)
        )
        assertThat(stateCaptor.allValues[3]).isEqualTo(
            State(data = emptyList(), loading = false, error = true, selected = null)
        )
    }

    @Test
    fun `GIVEN view model WHEN on click retry successfully THEN should emit loading and items`() {
        given(useCase.getData()).willReturn(Single.error(Exception()))

        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()
        val joke = Joke(joke = "Joke", setup = null, delivery = null)
        given(useCase.getData()).willReturn(Single.just(listOf(joke)))
        onClickRetryCaptor.allValues[1].invoke()

        assertThat(viewModelStream.values().size).isEqualTo(4)
        assertThat(stateCaptor.allValues[2]).isEqualTo(
            State(data = emptyList(), loading = true, error = false, selected = null)
        )
        assertThat(stateCaptor.allValues[3]).isEqualTo(
            State(data = listOf(joke), loading = false, error = false, selected = null)
        )
    }

    @Test
    fun `GIVEN view model WHEN on click item THEN should emit state with selected`() {
        val joke = Joke(joke = "Joke", setup = null, delivery = null)
        given(useCase.getData()).willReturn(Single.just(listOf(joke)))

        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()
        val selectedItem = JokeUiModel(joke = "Joke", setup = null, delivery = null, onClick = {})
        onClickItemCaptor.allValues[1].invoke(selectedItem)

        assertThat(viewModelStream.values().size).isEqualTo(3)
        assertThat(stateCaptor.allValues[2]).isEqualTo(
            State(data = listOf(joke), loading = false, error = false, selected = selectedItem)
        )
    }

    @Test
    fun `GIVEN view model WHEN on close item details THEN should emit state without selected`() {
        val joke = Joke(joke = "Joke", setup = null, delivery = null)
        given(useCase.getData()).willReturn(Single.just(listOf(joke)))

        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()
        val selectedItem = JokeUiModel(joke = "Joke", setup = null, delivery = null, onClick = {})
        onClickItemCaptor.allValues[1].invoke(selectedItem)
        onCloseItemDetailsCaptor.allValues[2].invoke()

        assertThat(viewModelStream.values().size).isEqualTo(4)
        assertThat(stateCaptor.allValues[3]).isEqualTo(
            State(data = listOf(joke), loading = false, error = false, selected = null)
        )
    }

    @Test
    fun `GIVEN view model WHEN on swipe refresh successfully THEN should emit state with new data`() {
        val joke = Joke(joke = "Joke", setup = null, delivery = null)
        given(useCase.getData()).willReturn(Single.just(listOf(joke)))

        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()
        val joke2 = Joke(joke = "Joke 2", setup = null, delivery = null)
        given(useCase.getData()).willReturn(Single.just(listOf(joke2)))
        onSwipeRefreshCaptor.allValues[1].invoke()

        assertThat(viewModelStream.values().size).isEqualTo(4)
        assertThat(stateCaptor.allValues[0]).isEqualTo(
            State(data = emptyList(), loading = true, error = false, selected = null)
        )
        assertThat(stateCaptor.allValues[1]).isEqualTo(
            State(data = listOf(joke), loading = false, error = false, selected = null)
        )
        assertThat(stateCaptor.allValues[2]).isEqualTo(
            State(data = emptyList(), loading = true, error = false, selected = null)
        )
        assertThat(stateCaptor.allValues[3]).isEqualTo(
            State(data = listOf(joke2), loading = false, error = false, selected = null)
        )
    }

    @Test
    fun `GIVEN view model WHEN on swipe refresh with error THEN should emit state with error`() {
        val joke = Joke(joke = "Joke", setup = null, delivery = null)
        given(useCase.getData()).willReturn(Single.just(listOf(joke)))

        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()
        given(useCase.getData()).willReturn(Single.error(Exception()))
        onSwipeRefreshCaptor.allValues[1].invoke()

        assertThat(viewModelStream.values().size).isEqualTo(4)
        assertThat(stateCaptor.allValues[0]).isEqualTo(
            State(data = emptyList(), loading = true, error = false, selected = null)
        )
        assertThat(stateCaptor.allValues[1]).isEqualTo(
            State(data = listOf(joke), loading = false, error = false, selected = null)
        )
        assertThat(stateCaptor.allValues[2]).isEqualTo(
            State(data = emptyList(), loading = true, error = false, selected = null)
        )
        assertThat(stateCaptor.allValues[3]).isEqualTo(
            State(data = emptyList(), loading = false, error = true, selected = null)
        )
    }
}
