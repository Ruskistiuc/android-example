package com.example.androidexample.domain

import com.example.androidexample.domain.models.Joke
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class JokesUseCaseImpl @Inject constructor(
    private val repository: JokesRepository
) : JokesUseCase {

    override fun getData(): Single<List<Joke>> {
        return repository.getData()
    }
}
