package com.example.androidexample.domain

import com.example.androidexample.domain.models.DomainObject
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class UseCaseImpl @Inject constructor(
    private val repository: Repository
) : UseCase {

    override fun getData(): Single<List<DomainObject>> {
        return repository.getData()
    }
}
