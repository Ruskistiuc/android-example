package com.example.androidexample.di

import com.example.androidexample.domain.JokesUseCase
import com.example.androidexample.domain.JokesUseCaseImpl
import com.example.androidexample.presentation.mapper.PresentationModelMapper
import com.example.androidexample.presentation.mapper.PresentationModelMapperImpl
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
    abstract fun bindPresentationModelMapper(
        presentationModelMapper: PresentationModelMapperImpl
    ): PresentationModelMapper

    @ViewModelScoped
    @Binds
    abstract fun bindUseCase(useCase: JokesUseCaseImpl): JokesUseCase
}
