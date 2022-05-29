package com.example.androidexample.presentation.mapper

import com.example.androidexample.domain.models.DomainObject
import com.example.androidexample.presentation.MainViewModel.State
import com.example.androidexample.presentation.models.PresentationItemModel
import com.example.androidexample.presentation.models.PresentationModel
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

        val onClickRetry = mock<() -> Unit>()
        val onClickItem = mock<(PresentationItemModel) -> Unit>()
        val onCloseItemDetails = mock<() -> Unit>()
        val onSwipeRefresh = mock<() -> Unit>()

        // Act
        val mapped = mapper.transform(
            state = state,
            onClickRetry = onClickRetry,
            onClickItem = onClickItem,
            onCloseItemDetails = onCloseItemDetails,
            onSwipeRefresh = onSwipeRefresh
        )

        // Assert
        assertThat(mapped).isEqualTo(
            PresentationModel(
                items = listOf(
                    PresentationItemModel(
                        joke = "joke",
                        setup = null,
                        delivery = null,
                        onClick = onClickItem
                    ),
                    PresentationItemModel(
                        joke = null,
                        setup = "setup",
                        delivery = "delivery",
                        onClick = onClickItem
                    )
                ),
                loading = true,
                error = false,
                onClickRetry = onClickRetry,
                selected = null,
                onCloseItemDetails = onCloseItemDetails,
                onSwipeRefresh = onSwipeRefresh
            )
        )
    }

    @Test
    fun `GIVEN mapper WHEN on click retry is called THEN should call the passed on click retry`() {
        // Arrange
        val state = mock<State>()
        given(state.data).willReturn(listOf(mock()))

        val onClickRetry = mock<() -> Unit>()

        // Act
        val mapped = mapper.transform(
            state = state,
            onClickRetry = onClickRetry,
            onClickItem = mock(),
            onCloseItemDetails = mock(),
            onSwipeRefresh = mock()
        )
        mapped.onClickRetry.invoke()

        // Assert
        verify(onClickRetry).invoke()
    }

    @Test
    fun `GIVEN mapper WHEN on click item is called THEN should call the passed on click item`() {
        // Arrange
        val state = mock<State>()
        given(state.data).willReturn(listOf(mock()))

        val onClickItem = mock<(PresentationItemModel) -> Unit>()

        val clickedItem = mock<PresentationItemModel>()

        // Act
        val mapped = mapper.transform(
            state = state,
            onClickRetry = mock(),
            onClickItem = onClickItem,
            onCloseItemDetails = mock(),
            onSwipeRefresh = mock()
        )
        mapped.items[0].onClick.invoke(clickedItem)

        // Assert
        verify(onClickItem).invoke(clickedItem)
    }

    @Test
    fun `GIVEN mapper WHEN on close item details is called THEN should call the passed on close item details`() {
        // Arrange
        val state = mock<State>()
        given(state.data).willReturn(listOf(mock()))

        val onCloseItemDetails = mock<() -> Unit>()

        // Act
        val mapped = mapper.transform(
            state = state,
            onClickRetry = mock(),
            onClickItem = mock(),
            onCloseItemDetails = onCloseItemDetails,
            onSwipeRefresh = mock()
        )
        mapped.onCloseItemDetails.invoke()

        // Assert
        verify(onCloseItemDetails).invoke()
    }

    @Test
    fun `GIVEN mapper WHEN on swipe refresh is called THEN should call the passed on swipe refresh`() {
        // Arrange
        val state = mock<State>()
        given(state.data).willReturn(listOf(mock()))

        val onSwipeRefresh = mock<() -> Unit>()

        // Act
        val mapped = mapper.transform(
            state = state,
            onClickRetry = mock(),
            onClickItem = mock(),
            onCloseItemDetails = mock(),
            onSwipeRefresh = onSwipeRefresh
        )
        mapped.onSwipeRefresh.invoke()

        // Assert
        verify(onSwipeRefresh).invoke()
    }
}
