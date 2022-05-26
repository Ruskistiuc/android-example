package com.example.androidexample.di

import com.example.androidexample.data.JokesRepositoryImpl
import com.example.androidexample.domain.JokesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun bindRepository(
        repository: JokesRepositoryImpl
    ): JokesRepository
}
