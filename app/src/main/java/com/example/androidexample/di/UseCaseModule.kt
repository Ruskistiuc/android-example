package com.example.androidexample.di

import com.example.androidexample.domain.JokesUseCase
import com.example.androidexample.domain.JokesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @ViewModelScoped
    @Binds
    abstract fun bindJokesUseCase(useCase: JokesUseCaseImpl): JokesUseCase
}
