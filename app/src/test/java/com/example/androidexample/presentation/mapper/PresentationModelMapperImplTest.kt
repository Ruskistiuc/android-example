package com.example.androidexample.presentation.mapper

import com.example.androidexample.domain.models.DomainObject
import com.example.androidexample.presentation.MainViewModel.State
import com.example.androidexample.presentation.models.PresentationItemModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class PresentationModelMapperImplTest {

    private lateinit var mapper: PresentationModelMapper

    @Before
    fun setup() {
        mapper = PresentationModelMapperImpl()
    }

    @Test
    fun `GIVEN mapper WHEN state is transformed THEN should return presentation model`() {
        // Arrange
        val domainObject1 = mock<DomainObject>()
        given(domainObject1.joke).willReturn("joke")

        val domainObject2 = mock<DomainObject>()
        given(domainObject2.setup).willReturn("setup")
        given(domainObject2.delivery).willReturn("delivery")

        val state = mock<State>()
        given(state.data).willReturn(listOf(domainObject1, domainObject2))
        given(state.loading).willReturn(true)
        given(state.error).willReturn(false)

        // Act
        val mapped = mapper.transform(state, mock())

        // Assert
        assertThat(mapped.items).isEqualTo(
            listOf(
                PresentationItemModel(
                    joke = "joke",
                    setup = null,
                    delivery = null
                ),
                PresentationItemModel(
                    joke = null,
                    setup = "setup",
                    delivery = "delivery"
                )
            )
        )
        assertThat(mapped.loading).isTrue
        assertThat(mapped.error).isFalse
    }

    @Test
    fun `GIVEN mapper WHEN on click retry is called THEN should call the passed on click retry`() {
        // Arrange
        val state = mock<State>()
        given(state.data).willReturn(listOf(mock()))

        val onClickRetry = mock<() -> Unit>()

        // Act
        val mapped = mapper.transform(state, onClickRetry)
        mapped.onClickRetry.invoke()

        // Assert
        verify(onClickRetry).invoke()
    }
}
