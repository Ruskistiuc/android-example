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
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.given
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private val useCase = mock<JokesUseCase>()
    private val mapper = mock<MainUiModelMapper>()

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelStream: TestObserver<MainUiModel>

    @Test
    fun `GIVEN view model WHEN init successfully THEN should emit loading and items`() {
        // Arrange
        given(useCase.getData()).willReturn(Single.just(listOf(mock())))
        val stateCaptor = argumentCaptor<State>()
        given(
            mapper.transform(
                state = stateCaptor.capture(),
                onClickRetry = any(),
                onClickItem = any(),
                onCloseItemDetails = any(),
                onSwipeRefresh = any()
            )
        ).willReturn(mock())

        // Act
        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(2)

        with(stateCaptor.allValues[0]) {
            assertThat(data).isEmpty()
            assertThat(loading).isTrue
            assertThat(error).isFalse
            assertThat(selected).isNull()
        }

        with(stateCaptor.allValues[1]) {
            assertThat(data.size).isEqualTo(1)
            assertThat(loading).isFalse
            assertThat(error).isFalse
            assertThat(selected).isNull()
        }
    }

    @Test
    fun `GIVEN view model WHEN init with error THEN should emit loading and error`() {
        // Arrange
        given(useCase.getData()).willReturn(Single.error(Exception()))
        val stateCaptor = argumentCaptor<State>()
        given(
            mapper.transform(
                state = stateCaptor.capture(),
                onClickRetry = any(),
                onClickItem = any(),
                onCloseItemDetails = any(),
                onSwipeRefresh = any()
            )
        ).willReturn(mock())

        // Act
        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(2)

        with(stateCaptor.allValues[0]) {
            assertThat(data).isEmpty()
            assertThat(loading).isTrue
            assertThat(error).isFalse
            assertThat(selected).isNull()
        }

        with(stateCaptor.allValues[1]) {
            assertThat(data).isEmpty()
            assertThat(loading).isFalse
            assertThat(error).isTrue
            assertThat(selected).isNull()
        }
    }

    @Test
    fun `GIVEN view model WHEN on click retry with error THEN should emit loading and error`() {
        // Arrange
        given(useCase.getData()).willReturn(Single.error(Exception()))

        val stateCaptor = argumentCaptor<State>()
        val onClickRetryCaptor = argumentCaptor<() -> Unit>()
        given(
            mapper.transform(
                state = stateCaptor.capture(),
                onClickRetry = onClickRetryCaptor.capture(),
                onClickItem = any(),
                onCloseItemDetails = any(),
                onSwipeRefresh = any()
            )
        ).willReturn(mock())

        // Act
        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()

        onClickRetryCaptor.lastValue.invoke()

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(4)

        with(stateCaptor.allValues[2]) {
            assertThat(data).isEmpty()
            assertThat(loading).isTrue
            assertThat(error).isFalse
            assertThat(selected).isNull()
        }

        with(stateCaptor.allValues[3]) {
            assertThat(data).isEmpty()
            assertThat(loading).isFalse
            assertThat(error).isTrue
            assertThat(selected).isNull()
        }
    }

    @Test
    fun `GIVEN view model WHEN on click retry successfully THEN should emit loading and items`() {
        // Arrange
        given(useCase.getData()).willReturn(Single.error(Exception()))

        val stateCaptor = argumentCaptor<State>()
        val onClickRetryCaptor = argumentCaptor<() -> Unit>()
        given(
            mapper.transform(
                state = stateCaptor.capture(),
                onClickRetry = onClickRetryCaptor.capture(),
                onClickItem = any(),
                onCloseItemDetails = any(),
                onSwipeRefresh = any()
            )
        ).willReturn(mock())

        // Act
        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()

        given(useCase.getData()).willReturn(Single.just(listOf(mock())))
        onClickRetryCaptor.lastValue.invoke()

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(4)

        with(stateCaptor.allValues[2]) {
            assertThat(data).isEmpty()
            assertThat(loading).isTrue
            assertThat(error).isFalse
            assertThat(selected).isNull()
        }

        with(stateCaptor.allValues[3]) {
            assertThat(data.size).isEqualTo(1)
            assertThat(loading).isFalse
            assertThat(error).isFalse
            assertThat(selected).isNull()
        }
    }

    @Test
    fun `GIVEN view model WHEN on click item THEN should emit state with selected`() {
        // Arrange
        given(useCase.getData()).willReturn(Single.just(listOf(mock())))

        val stateCaptor = argumentCaptor<State>()
        val onClickItemCaptor = argumentCaptor<(JokeUiModel) -> Unit>()
        given(
            mapper.transform(
                state = stateCaptor.capture(),
                onClickRetry = any(),
                onClickItem = onClickItemCaptor.capture(),
                onCloseItemDetails = any(),
                onSwipeRefresh = any()
            )
        ).willReturn(mock())

        val selectedItem = mock<JokeUiModel>()

        // Act
        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()

        onClickItemCaptor.lastValue.invoke(selectedItem)

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(3)

        with(stateCaptor.allValues[2]) {
            assertThat(data.size).isEqualTo(1)
            assertThat(loading).isFalse
            assertThat(error).isFalse
            assertThat(selected).isEqualTo(selectedItem)
        }
    }

    @Test
    fun `GIVEN view model WHEN on close item details THEN should emit state without selected`() {
        // Arrange
        given(useCase.getData()).willReturn(Single.just(listOf(mock())))

        val stateCaptor = argumentCaptor<State>()
        val onClickItemCaptor = argumentCaptor<(JokeUiModel) -> Unit>()
        val onCloseItemDetailsCaptor = argumentCaptor<() -> Unit>()

        val selectedItem = mock<JokeUiModel>()

        given(
            mapper.transform(
                state = stateCaptor.capture(),
                onClickRetry = any(),
                onClickItem = onClickItemCaptor.capture(),
                onCloseItemDetails = onCloseItemDetailsCaptor.capture(),
                onSwipeRefresh = any()
            )
        ).willReturn(mock())

        // Act
        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()

        onClickItemCaptor.lastValue.invoke(selectedItem)

        onCloseItemDetailsCaptor.lastValue.invoke()

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(4)

        with(stateCaptor.allValues[3]) {
            assertThat(data.size).isEqualTo(1)
            assertThat(loading).isFalse
            assertThat(error).isFalse
            assertThat(selected).isNull()
        }
    }

    @Test
    fun `GIVEN view model WHEN on swipe refresh successfully THEN should emit state with new data`() {
        // Arrange
        given(useCase.getData()).willReturn(Single.just(listOf(mock())))

        val stateCaptor = argumentCaptor<State>()
        val onSwipeRefreshCaptor = argumentCaptor<() -> Unit>()
        given(
            mapper.transform(
                state = stateCaptor.capture(),
                onClickRetry = any(),
                onClickItem = any(),
                onCloseItemDetails = any(),
                onSwipeRefresh = onSwipeRefreshCaptor.capture()
            )
        ).willReturn(mock())

        // Act
        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()

        val newItem = mock<Joke>()
        given(useCase.getData()).willReturn(Single.just(listOf(newItem)))
        onSwipeRefreshCaptor.lastValue.invoke()

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(4)

        with(stateCaptor.allValues[2]) {
            assertThat(data).isEmpty()
            assertThat(loading).isTrue
            assertThat(error).isFalse
            assertThat(selected).isNull()
        }

        with(stateCaptor.allValues[3]) {
            assertThat(data.size).isEqualTo(1)
            assertThat(data.first()).isEqualTo(newItem)
            assertThat(loading).isFalse
            assertThat(error).isFalse
            assertThat(selected).isNull()
        }
    }

    @Test
    fun `GIVEN view model WHEN on swipe refresh with error THEN should emit state with error`() {
        // Arrange
        given(useCase.getData()).willReturn(Single.just(listOf(mock())))

        val stateCaptor = argumentCaptor<State>()
        val onSwipeRefreshCaptor = argumentCaptor<() -> Unit>()
        given(
            mapper.transform(
                state = stateCaptor.capture(),
                onClickRetry = any(),
                onClickItem = any(),
                onCloseItemDetails = any(),
                onSwipeRefresh = onSwipeRefreshCaptor.capture()
            )
        ).willReturn(mock())

        // Act
        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.uiModel.test()

        given(useCase.getData()).willReturn(Single.error(Exception()))
        onSwipeRefreshCaptor.lastValue.invoke()

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(4)

        with(stateCaptor.allValues[2]) {
            assertThat(data).isEmpty()
            assertThat(loading).isTrue
            assertThat(error).isFalse
            assertThat(selected).isNull()
        }

        with(stateCaptor.allValues[3]) {
            assertThat(data).isEmpty()
            assertThat(loading).isFalse
            assertThat(error).isTrue
            assertThat(selected).isNull()
        }
    }
}
