package com.example.androidexample.domain

import com.example.androidexample.domain.models.Joke
import io.reactivex.rxjava3.core.Single

interface JokesUseCase {

    fun getData(): Single<List<Joke>>
}
