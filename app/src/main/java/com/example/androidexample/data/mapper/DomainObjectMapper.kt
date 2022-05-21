package com.example.androidexample.data.mapper

import com.example.androidexample.data.models.Joke
import com.example.androidexample.domain.models.DomainObject

interface DomainObjectMapper {

    fun transform(list: List<Joke>): List<DomainObject>
}
