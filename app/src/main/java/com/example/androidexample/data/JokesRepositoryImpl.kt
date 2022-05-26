package com.example.androidexample.data

import com.example.androidexample.data.mapper.DomainObjectMapper
import com.example.androidexample.domain.JokesRepository
import com.example.androidexample.domain.models.DomainObject
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class JokesRepositoryImpl @Inject constructor(
    private val service: JokesService,
    private val mapper: DomainObjectMapper
) : JokesRepository {

    override fun getData(): Single<List<DomainObject>> {
        return service.getData()
            .singleOrError()
            .map { response ->
                mapper.transform(response)
            }
    }
}
