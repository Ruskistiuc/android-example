package com.example.androidexample.presentation

import com.example.androidexample.domain.JokesUseCase
import com.example.androidexample.presentation.MainViewModel.State
import com.example.androidexample.presentation.mapper.PresentationModelMapper
import com.example.androidexample.presentation.models.PresentationModel
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
    private val mapper = mock<PresentationModelMapper>()

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelStream: TestObserver<PresentationModel>

    @Test
    fun `GIVEN view model WHEN init successfully THEN should emit loading and items`() {
        // Arrange
        given(useCase.getData()).willReturn(Single.just(listOf(mock())))
        val stateCaptor = argumentCaptor<State>()
        given(mapper.transform(stateCaptor.capture(), any())).willReturn(mock())

        // Act
        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.model.test()

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(2)

        with(stateCaptor.allValues[0]) {
            assertThat(data).isEmpty()
            assertThat(loading).isTrue
            assertThat(error).isFalse
        }

        with(stateCaptor.allValues[1]) {
            assertThat(data.size).isEqualTo(1)
            assertThat(loading).isFalse
            assertThat(error).isFalse
        }
    }

    @Test
    fun `GIVEN view model WHEN init with error THEN should emit loading and error`() {
        // Arrange
        given(useCase.getData()).willReturn(Single.error(Exception()))
        val stateCaptor = argumentCaptor<State>()
        given(mapper.transform(stateCaptor.capture(), any())).willReturn(mock())

        // Act
        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.model.test()

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(2)

        with(stateCaptor.allValues[0]) {
            assertThat(data).isEmpty()
            assertThat(loading).isTrue
            assertThat(error).isFalse
        }

        with(stateCaptor.allValues[1]) {
            assertThat(data).isEmpty()
            assertThat(loading).isFalse
            assertThat(error).isTrue
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
                onclickRetry = onClickRetryCaptor.capture()
            )
        ).willReturn(mock())

        // Act
        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.model.test()

        onClickRetryCaptor.lastValue.invoke()

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(4)

        with(stateCaptor.allValues[2]) {
            assertThat(data).isEmpty()
            assertThat(loading).isTrue
            assertThat(error).isFalse
        }

        with(stateCaptor.allValues[3]) {
            assertThat(data).isEmpty()
            assertThat(loading).isFalse
            assertThat(error).isTrue
        }
    }

    @Test
    fun `GIVEN view model WHEN on click retry with success THEN should emit loading and items`() {
        // Arrange
        given(useCase.getData()).willReturn(Single.error(Exception()))

        val stateCaptor = argumentCaptor<State>()
        val onClickRetryCaptor = argumentCaptor<() -> Unit>()
        given(
            mapper.transform(
                state = stateCaptor.capture(),
                onclickRetry = onClickRetryCaptor.capture()
            )
        ).willReturn(mock())

        // Act
        viewModel = MainViewModel(useCase, mapper)
        viewModelStream = viewModel.model.test()

        given(useCase.getData()).willReturn(Single.just(listOf(mock())))
        onClickRetryCaptor.lastValue.invoke()

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(4)

        with(stateCaptor.allValues[2]) {
            assertThat(data).isEmpty()
            assertThat(loading).isTrue
            assertThat(error).isFalse
        }

        with(stateCaptor.allValues[3]) {
            assertThat(data.size).isEqualTo(1)
            assertThat(loading).isFalse
            assertThat(error).isFalse
        }
    }
}
