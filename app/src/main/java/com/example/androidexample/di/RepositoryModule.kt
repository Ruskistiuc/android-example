package com.example.androidexample.di

import com.example.androidexample.data.RepositoryImpl
import com.example.androidexample.domain.Repository
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
        repository: RepositoryImpl
    ): Repository
}
