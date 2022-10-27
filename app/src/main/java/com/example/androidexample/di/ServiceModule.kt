package com.example.androidexample.di

import com.example.androidexample.data.JokesService
import com.example.androidexample.di.ServiceModule.RetrofitModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [RetrofitModule::class])
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Module
    @InstallIn(SingletonComponent::class)
    internal object RetrofitModule {

        @Singleton
        @Provides
        fun provideJokesService(): JokesService {
            return Retrofit.Builder()
                .baseUrl("https://v2.jokeapi.dev/joke/")
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JokesService::class.java)
        }
    }
}
