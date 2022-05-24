package com.example.androidexample.data.mapper

import com.example.androidexample.data.models.Response
import com.example.androidexample.domain.models.DomainObject
import javax.inject.Inject

class DomainObjectMapperImpl @Inject constructor() : DomainObjectMapper {

    override fun transform(response: Response): List<DomainObject> {
        return response.jokes.map { item ->
            DomainObject(
                joke = item.joke,
                setup = item.setup,
                delivery = item.delivery
            )
        }
    }
}
