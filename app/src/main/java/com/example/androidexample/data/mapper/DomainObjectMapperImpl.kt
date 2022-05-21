package com.example.androidexample.data.mapper

import com.example.androidexample.data.models.Joke
import com.example.androidexample.domain.models.DomainObject
import javax.inject.Inject

class DomainObjectMapperImpl @Inject constructor() : DomainObjectMapper {

    override fun transform(list: List<Joke>): List<DomainObject> {
        return list.map { item ->
            DomainObject(
                setup = item.setup,
                punchline = item.punchline
            )
        }
    }
}
