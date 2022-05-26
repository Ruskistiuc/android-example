package com.example.androidexample.domain

import com.example.androidexample.domain.models.DomainObject
import io.reactivex.rxjava3.core.Single

interface JokesRepository {

    fun getData(): Single<List<DomainObject>>
}
