package com.example.androidexample.data.mapper

import com.example.androidexample.data.models.JokeEntity
import com.example.androidexample.data.models.Response
import com.example.androidexample.domain.models.Joke
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock

class ResponseMapperTest {

    private lateinit var mapper: ResponseMapper

    @Before
    fun setup() {
        mapper = ResponseMapper()
    }

    @Test
    fun `GIVEN mapper WHEN response is transformed THEN should return list of domain object`() {
        // Arrange
        val response = mock<Response>()
        val jokeEntity1 = mock<JokeEntity>()
        given(jokeEntity1.joke).willReturn("joke")

        val jokeEntity2 = mock<JokeEntity>()
        given(jokeEntity2.setup).willReturn("joke setup")
        given(jokeEntity2.delivery).willReturn("joke delivery")

        val list = listOf(jokeEntity1, jokeEntity2)
        given(response.jokes).willReturn(list)

        // Act
        val mapped = mapper.transform(response)

        // Assert
        assertThat(mapped).isEqualTo(
            listOf(
                Joke(
                    joke = "joke",
                    setup = null,
                    delivery = null
                ),
                Joke(
                    joke = null,
                    setup = "joke setup",
                    delivery = "joke delivery"
                )
            )
        )
    }
}
