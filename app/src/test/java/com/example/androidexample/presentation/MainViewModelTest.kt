package com.example.androidexample.presentation

import com.example.androidexample.domain.UseCase
import com.example.androidexample.domain.models.DomainObject
import com.example.androidexample.presentation.models.PresentationItemModel
import com.example.androidexample.presentation.models.PresentationModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private val useCase = mock<UseCase>()

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelStream: TestObserver<PresentationModel>

    @Test
    fun `GIVEN view model WHEN init successfully THEN should emit loading and items`() {
        // Arrange
        val domainObject = mock<DomainObject>()
        given(domainObject.setup).willReturn("setup")
        given(domainObject.punchline).willReturn("punchline")

        given(useCase.getData()).willReturn(Single.just(listOf(domainObject)))

        // Act
        viewModel = MainViewModel(useCase)
        viewModelStream = viewModel.model.test()

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(2)

        with(viewModelStream.values()[0]) {
            assertThat(items).isEmpty()
            assertThat(isLoading).isTrue
            assertThat(isError).isFalse
        }

        with(viewModelStream.values()[1]) {
            assertThat(items.size).isEqualTo(1)
            assertThat(isLoading).isFalse
            assertThat(isError).isFalse
        }
    }

    @Test
    fun `GIVEN view model WHEN state has items THEN should emit presentation model with items`() {
        // Arrange
        val domainObject = mock<DomainObject>()
        given(domainObject.setup).willReturn("setup")
        given(domainObject.punchline).willReturn("punchline")

        given(useCase.getData()).willReturn(Single.just(listOf(domainObject)))

        // Act
        viewModel = MainViewModel(useCase)
        viewModelStream = viewModel.model.test()

        // Assert
        assertThat(viewModelStream.values().last()).isEqualTo(
            PresentationModel(
                items = listOf(
                    PresentationItemModel(
                        setup = "setup",
                        punchline = "punchline"
                    )
                ),
                isLoading = false,
                isError = false
            )
        )
    }

    @Test
    fun `GIVEN view model WHEN init with error THEN should emit error`() {
        // Arrange
        given(useCase.getData()).willReturn(Single.error(Exception()))

        // Act
        viewModel = MainViewModel(useCase)
        viewModelStream = viewModel.model.test()

        // Assert
        assertThat(viewModelStream.values().size).isEqualTo(2)

        with(viewModelStream.values()[0]) {
            assertThat(items).isEmpty()
            assertThat(isLoading).isTrue
            assertThat(isError).isFalse
        }

        with(viewModelStream.values()[1]) {
            assertThat(items).isEmpty()
            assertThat(isLoading).isFalse
            assertThat(isError).isTrue
        }
    }
}
