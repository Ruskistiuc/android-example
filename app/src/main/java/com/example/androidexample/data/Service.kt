package com.example.androidexample.data

import com.example.androidexample.data.models.Joke
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface Service {

    @GET("programming/ten")
    fun getData(): Observable<List<Joke>>
}
