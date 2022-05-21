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
class UseCaseImplTest {

    private val repository = mock<Repository>()
    private lateinit var useCase: UseCase

    @Before
    fun setup() {
        useCase = UseCaseImpl(repository)
    }

    @Test
    fun `GIVEN use case WHEN data requested successfully THEN should return list of domain objects`() {
        // Arrange
        val data = listOf<DomainObject>(mock())
        given(repository.getData()).willReturn(Single.just(data))

        // Act
        val dataObserver: TestObserver<List<DomainObject>> = useCase.getData().test()

        // Assert
        assertThat(dataObserver.values().first()).isEqualTo(data)
    }
}
