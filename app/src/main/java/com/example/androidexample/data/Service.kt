package com.example.androidexample.data

import com.example.androidexample.data.models.Response
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface Service {

    @GET("/ten")
    fun getData(): Observable<Response>
}
