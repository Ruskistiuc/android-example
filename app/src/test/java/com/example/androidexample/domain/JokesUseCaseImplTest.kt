package com.example.androidexample.domain

import com.example.androidexample.domain.models.Joke
import io.reactivex.rxjava3.core.Single
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock

class JokesUseCaseImplTest {

    private val repository = mock<JokesRepository>()
    private val useCase = JokesUseCaseImpl(repository)

    @Test
    fun `GIVEN use case WHEN data requested successfully THEN should return list of domain objects`() {
        val jokes = listOf(Joke(joke = "Joke", setup = null, delivery = null))
        given(repository.getData()).willReturn(Single.just(jokes))

        useCase
            .getData()
            .test()
            .assertValue(jokes)
    }
}
