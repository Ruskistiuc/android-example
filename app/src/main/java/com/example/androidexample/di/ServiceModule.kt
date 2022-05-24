package com.example.androidexample.di

import com.example.androidexample.data.Service
import com.example.androidexample.data.mapper.DomainObjectMapper
import com.example.androidexample.data.mapper.DomainObjectMapperImpl
import com.example.androidexample.di.ServiceModule.RetrofitModule
import dagger.Binds
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

    @Singleton
    @Binds
    abstract fun bindDomainObjectMapper(
        domainObjectMapper: DomainObjectMapperImpl
    ): DomainObjectMapper

    @Module
    @InstallIn(SingletonComponent::class)
    internal object RetrofitModule {

        @Singleton
        @Provides
        fun provideService(): Service {
            return Retrofit.Builder()
                .baseUrl("https://v2.jokeapi.dev/joke/")
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Service::class.java)
        }
    }
}
