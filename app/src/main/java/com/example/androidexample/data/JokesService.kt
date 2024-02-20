package com.example.androidexample.data

import com.example.androidexample.data.models.Response
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface JokesService {

    @GET("Programming?amount=10")
    fun getData(): Single<Response>
}
