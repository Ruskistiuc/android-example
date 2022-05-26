package com.example.androidexample.domain

import com.example.androidexample.domain.models.DomainObject
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class JokesUseCaseImplTest {

    private val repository = mock<JokesRepository>()
    private lateinit var useCase: JokesUseCase

    @Before
    fun setup() {
        useCase = JokesUseCaseImpl(repository)
    }

    @Test
    fun `GIVEN use case WHEN data requested successfully THEN should return list of domain objects`() {
        // Arrange
        val data: List<DomainObject> = mock()
        given(repository.getData()).willReturn(Single.just(data))

        // Act
        val dataObserver: TestObserver<List<DomainObject>> = useCase.getData().test()

        // Assert
        assertThat(dataObserver.values().first()).isEqualTo(data)
    }
}
