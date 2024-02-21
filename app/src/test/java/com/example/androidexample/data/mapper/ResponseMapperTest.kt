package com.example.androidexample.data.mapper

import com.example.androidexample.data.models.JokeEntity
import com.example.androidexample.data.models.Response
import com.example.androidexample.domain.models.Joke
import java.util.stream.Stream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource

class ResponseMapperTest {

    private val mapper = ResponseMapper()

    @ParameterizedTest(name = "Given: {0} - Expected: {1}")
    @ArgumentsSource(JokesArgumentsProvider::class)
    fun `GIVEN mapper WHEN response is transformed THEN should return list of domain object`(
        givenJokesEntity: JokeEntity,
        expectedJoke: Joke,
    ) {
        val response = Response(jokes = listOf(givenJokesEntity))

        assertThat(mapper.transform(response)).isEqualTo(listOf(expectedJoke))
    }

    private class JokesArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    JokeEntity(joke = "Joke", setup = null, delivery = null),
                    Joke(joke = "Joke", setup = null, delivery = null),
                ),
                Arguments.of(
                    JokeEntity(joke = null, setup = "Setup", delivery = "Delivery"),
                    Joke(joke = null, setup = "Setup", delivery = "Delivery"),
                ),
            )
        }
    }
}
