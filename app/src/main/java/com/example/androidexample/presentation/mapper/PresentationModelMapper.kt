package com.example.androidexample.presentation.mapper

import com.example.androidexample.presentation.MainViewModel.State
import com.example.androidexample.presentation.models.PresentationModel

interface PresentationModelMapper {

    fun transform(
        state: State,
        onclickRetry: () -> Unit
    ): PresentationModel
}
