package com.example.androidexample.di

import com.example.androidexample.data.RepositoryImpl
import com.example.androidexample.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(
        repository: RepositoryImpl
    ): Repository
}
