package com.example.androidexample.data.mapper

import com.example.androidexample.data.models.JokeEntity
import com.example.androidexample.data.models.Response
import com.example.androidexample.domain.models.Joke
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ResponseMapperTest {

    private val mapper = ResponseMapper()

    @Test
    fun `GIVEN mapper WHEN response is transformed THEN should return list of domain object`() {
        val response = Response(
            jokes = listOf(
                JokeEntity(joke = "Joke", setup = null, delivery = null),
                JokeEntity(joke = null, setup = "Setup", delivery = "Delivery"),
            )
        )

        assertThat(mapper.transform(response)).isEqualTo(
            listOf(
                Joke(joke = "Joke", setup = null, delivery = null),
                Joke(joke = null, setup = "Setup", delivery = "Delivery"),
            )
        )
    }
}
