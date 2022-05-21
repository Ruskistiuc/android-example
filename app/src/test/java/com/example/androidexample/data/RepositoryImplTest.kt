package com.example.androidexample.data

import com.example.androidexample.data.mapper.DomainObjectMapper
import com.example.androidexample.data.models.Joke
import com.example.androidexample.data.models.Response
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
        val response = mock<Response>()
        given(service.getData()).willReturn(Observable.just(response))

        val responseList = listOf<Joke>(mock())
        given(response.list).willReturn(responseList)

        val list = listOf<DomainObject>(mock())
        given(mapper.transform(response)).willReturn(list)

        // Act
        val dataObserver: TestObserver<List<DomainObject>> = repository.getData().test()

        // Assert
        assertThat(dataObserver.values().first()).isEqualTo(list)
    }

    @Test
    fun `GIVEN repository WHEN data requested is null THEN should throw an exception`() {
        // Arrange
        val response = mock<Response>()
        given(response.list).willReturn(null)
        given(service.getData()).willReturn(Observable.just(response))

        // Act
        val dataObserver: TestObserver<List<DomainObject>> = repository.getData().test()

        // Assert
        dataObserver.assertError(Exception::class.java)
    }

    @Test
    fun `GIVEN repository WHEN data requested is empty THEN should throw an exception`() {
        // Arrange
        val response = mock<Response>()
        given(response.list).willReturn(emptyList())
        given(service.getData()).willReturn(Observable.just(response))

        // Act
        val dataObserver: TestObserver<List<DomainObject>> = repository.getData().test()

        // Assert
        dataObserver.assertError(Exception::class.java)
    }
}
