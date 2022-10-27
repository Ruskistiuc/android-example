package com.example.androidexample.data.mapper

import com.example.androidexample.data.models.Response
import com.example.androidexample.domain.models.Joke
import javax.inject.Inject

class ResponseMapper @Inject constructor() {

    fun transform(response: Response): List<Joke> {
        return response.jokes.map { jokeEntity ->
            Joke(
                joke = jokeEntity.joke,
                setup = jokeEntity.setup,
                delivery = jokeEntity.delivery
            )
        }
    }
}
