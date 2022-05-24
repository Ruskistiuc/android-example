package com.example.androidexample.data.mapper

import com.example.androidexample.data.models.Joke
import com.example.androidexample.data.models.Response
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
    fun `GIVEN mapper WHEN response is transformed THEN should return list of domain object`() {
        // Arrange
        val response = mock<Response>()
        val joke1 = mock<Joke>()
        given(joke1.joke).willReturn("joke")

        val joke2 = mock<Joke>()
        given(joke2.setup).willReturn("joke setup")
        given(joke2.delivery).willReturn("joke delivery")

        val list = listOf(joke1, joke2)
        given(response.jokes).willReturn(list)

        // Act
        val mapped = mapper.transform(response)

        // Assert
        assertThat(mapped).isEqualTo(
            listOf(
                DomainObject(
                    joke = "joke",
                    setup = null,
                    delivery = null
                ),
                DomainObject(
                    joke = null,
                    setup = "joke setup",
                    delivery = "joke delivery"
                )
            )
        )
    }
}
