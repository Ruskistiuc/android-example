package com.example.androidexample.data

import com.example.androidexample.data.mapper.DomainObjectMapper
import com.example.androidexample.data.models.Joke
import com.example.androidexample.domain.Repository
import com.example.androidexample.domain.models.DomainObject
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

/**
 * This annotation tells the Mockito test runner to validate that your usage
 * of the framework is correct and simplifies the initialization of your mock objects.
 */
@RunWith(MockitoJUnitRunner::class)
class RepositoryImplTest {

    private val service = mock<Service>()
    private val mapper = mock<DomainObjectMapper>()

    private lateinit var repository: Repository

    @Before
    fun setup() {
        repository = RepositoryImpl(
            service = service,
            mapper = mapper
        )
    }

    @Test
    fun `GIVEN repository WHEN data requested successfully THEN transformed data should be returned`() {
        // Arrange
        val list = listOf<Joke>(mock())
        given(service.getData()).willReturn(Observable.just(list))

        val transformedList = listOf<DomainObject>(mock())
        given(mapper.transform(list)).willReturn(transformedList)

        // Act
        val dataObserver: TestObserver<List<DomainObject>> = repository.getData().test()

        // Assert
        assertThat(dataObserver.values().first()).isEqualTo(transformedList)
    }

    @Test
    fun `GIVEN repository WHEN data requested with error THEN should throw an exception`() {
        // Arrange
        given(service.getData()).willReturn(Observable.error(Exception()))

        // Act
        val dataObserver: TestObserver<List<DomainObject>> = repository.getData().test()

        // Assert
        dataObserver.assertError(Exception::class.java)
    }
}
