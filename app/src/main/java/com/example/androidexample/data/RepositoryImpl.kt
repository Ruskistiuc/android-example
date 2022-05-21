package com.example.androidexample.data

import com.example.androidexample.data.mapper.DomainObjectMapper
import com.example.androidexample.domain.Repository
import com.example.androidexample.domain.models.DomainObject
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val service: Service,
    private val mapper: DomainObjectMapper
) : Repository {

    override fun getData(): Single<List<DomainObject>> {
        return service.getData()
            .singleOrError()
            .map { response ->
                if (response.list.isNullOrEmpty()) {
                    throw Exception("Error loading data")
                } else {
                    mapper.transform(response)
                }
            }
    }
}
