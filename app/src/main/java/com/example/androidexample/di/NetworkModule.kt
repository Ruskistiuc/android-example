package com.example.androidexample.di

import com.example.androidexample.data.Service
import com.example.androidexample.data.mapper.DomainObjectMapper
import com.example.androidexample.data.mapper.DomainObjectMapperImpl
import com.example.androidexample.di.NetworkModule.ServiceModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ServiceModule::class])
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    abstract fun provideDomainObjectMapper(
        domainObjectMapper: DomainObjectMapperImpl
    ): DomainObjectMapper

    @Module
    @InstallIn(SingletonComponent::class)
    internal object ServiceModule {

        @Singleton
        @Provides
        fun provideService(): Service {
            return Retrofit.Builder()
                .baseUrl("https://karljoke.herokuapp.com/jokes/programming")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Service::class.java)
        }
    }
}
