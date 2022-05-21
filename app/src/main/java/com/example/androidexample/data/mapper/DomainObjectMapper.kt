package com.example.androidexample.data.mapper

import com.example.androidexample.data.models.Response
import com.example.androidexample.domain.models.DomainObject

interface DomainObjectMapper {

    fun transform(response: Response): List<DomainObject>
}
