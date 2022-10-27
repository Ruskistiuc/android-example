package com.example.androidexample.data

import com.example.androidexample.data.mapper.ResponseMapper
import com.example.androidexample.domain.JokesRepository
import com.example.androidexample.domain.models.Joke
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class JokesRepositoryImpl @Inject constructor(
    private val service: JokesService,
    private val mapper: ResponseMapper
) : JokesRepository {

    override fun getData(): Single<List<Joke>> {
        return service.getData()
            .singleOrError()
            .map { response -> mapper.transform(response) }
    }
}
