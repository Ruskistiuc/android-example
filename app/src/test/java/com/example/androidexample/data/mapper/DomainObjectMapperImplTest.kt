package com.example.androidexample.data.mapper

import com.example.androidexample.data.models.Joke
import com.example.androidexample.domain.models.DomainObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock

class DomainObjectMapperImplTest {

    private lateinit var mapper: DomainObjectMapper

    @Before
    fun setup() {
        mapper = DomainObjectMapperImpl()
    }

    @Test
    fun `GIVEN mapper WHEN list is transformed THEN should return list of domain object`() {
        // Arrange
        val joke = mock<Joke>()
        given(joke.setup).willReturn("joke setup")
        given(joke.punchline).willReturn("joke punchline")
        val list = listOf(joke)

        // Act
        val mapped = mapper.transform(list)

        // Assert
        assertThat(mapped).isEqualTo(
            listOf(
                DomainObject(
                    setup = "joke setup",
                    punchline = "joke punchline"
                )
            )
        )
    }
}
