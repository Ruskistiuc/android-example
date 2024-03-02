package com.example.androidexample.data

import com.example.androidexample.data.mapper.ResponseMapper
import com.example.androidexample.data.models.JokeEntity
import com.example.androidexample.data.models.Response
import com.example.androidexample.domain.models.Joke
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.kotlin.mock

class JokesRepositoryImplTest {

    private val service = mock<JokesService>()
    private val mapper = mock<ResponseMapper>()
    private val repository = JokesRepositoryImpl(service, mapper)

    @Test
    fun `GIVEN repository WHEN data requested successfully THEN transformed data should be returned`() {
        val response = Response(
            jokes = listOf(JokeEntity(joke = "Joke", setup = null, delivery = null))
        )
        given(service.getData()).willReturn(Single.just(response))
        val jokes = listOf(Joke(joke = "Joke", setup = null, delivery = null))
        given(mapper.transform(response)).willReturn(jokes)

        repository
            .getData()
            .test()
            .assertValue(jokes)
    }

    @Test
    fun `GIVEN repository WHEN data requested with error THEN should throw an exception`() {
        given(service.getData()).willReturn(Single.error(Exception()))

        repository
            .getData()
            .test()
            .assertError(Exception::class.java)
    }
}
