package com.example.androidexample.di

import com.example.androidexample.domain.UseCase
import com.example.androidexample.domain.UseCaseImpl
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
    abstract fun bindUseCase(useCase: UseCaseImpl): UseCase
}
