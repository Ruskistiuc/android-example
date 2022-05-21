package com.example.androidexample.data.mapper

import com.example.androidexample.data.models.Response
import com.example.androidexample.domain.models.DomainObject
import javax.inject.Inject

class DomainObjectMapperImpl @Inject constructor() : DomainObjectMapper {

    override fun transform(response: Response): List<DomainObject> {
        return response.list.map { item ->
            DomainObject(
                setup = item.setup,
                punchline = item.punchline
            )
        }
    }
}
