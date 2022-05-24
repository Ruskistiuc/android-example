package com.example.androidexample.presentation.mapper

import com.example.androidexample.presentation.MainViewModel.State
import com.example.androidexample.presentation.models.PresentationItemModel
import com.example.androidexample.presentation.models.PresentationModel
import javax.inject.Inject

class PresentationModelMapperImpl @Inject constructor() : PresentationModelMapper {

    override fun transform(
        state: State,
        onclickRetry: () -> Unit
    ): PresentationModel {
        return PresentationModel(
            items = state.data.map { item ->
                PresentationItemModel(
                    joke = item.joke,
                    setup = item.setup,
                    delivery = item.delivery
                )
            },
            loading = state.loading,
            error = state.error,
            onClickRetry = onclickRetry
        )
    }
}
