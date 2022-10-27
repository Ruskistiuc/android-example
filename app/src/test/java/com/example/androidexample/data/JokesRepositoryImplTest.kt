package com.example.androidexample.data

import com.example.androidexample.data.mapper.ResponseMapper
import com.example.androidexample.data.models.Response
import com.example.androidexample.domain.JokesRepository
import com.example.androidexample.domain.models.Joke
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
class JokesRepositoryImplTest {

    private val service = mock<JokesService>()
    private val mapper = mock<ResponseMapper>()

    private lateinit var repository: JokesRepository

    @Before
    fun setup() {
        repository = JokesRepositoryImpl(
            service = service,
            mapper = mapper
        )
    }

    @Test
    fun `GIVEN repository WHEN data requested successfully THEN transformed data should be returned`() {
        // Arrange
        val response = mock<Response>()
        given(service.getData()).willReturn(Observable.just(response))

        val transformedList = listOf<Joke>(mock())
        given(mapper.transform(response)).willReturn(transformedList)

        // Act
        val dataObserver: TestObserver<List<Joke>> = repository.getData().test()

        // Assert
        assertThat(dataObserver.values().first()).isEqualTo(transformedList)
    }

    @Test
    fun `GIVEN repository WHEN data requested with error THEN should throw an exception`() {
        // Arrange
        given(service.getData()).willReturn(Observable.error(Exception()))

        // Act
        val dataObserver: TestObserver<List<Joke>> = repository.getData().test()

        // Assert
        dataObserver.assertError(Exception::class.java)
    }
}
