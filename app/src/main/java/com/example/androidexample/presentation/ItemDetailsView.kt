package com.example.androidexample.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.androidexample.presentation.models.ScreenState.Details.JokeDetailsUiModel
import com.example.androidexample.ui.theme.AndroidExampleTheme

const val ITEM_DETAILS_VIEW_ITEM = "itemDetails"
const val ITEM_DETAILS_VIEW_BACK_BUTTON = "backButton"

@Composable
fun ItemDetailsView(
    item: JokeDetailsUiModel,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag(ITEM_DETAILS_VIEW_BACK_BUTTON)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    start = AndroidExampleTheme.paddings.padding_l,
                    end = AndroidExampleTheme.paddings.padding_l,
                    bottom = AndroidExampleTheme.paddings.padding_l
                )
                .testTag(ITEM_DETAILS_VIEW_ITEM),
            verticalArrangement = Arrangement.Center
        ) {
            item.joke?.let { joke ->
                Text(
                    text = joke,
                    fontWeight = FontWeight.Bold,
                    style = AndroidExampleTheme.typography.h4,
                    modifier = Modifier.padding(AndroidExampleTheme.paddings.padding_xs)
                )
            }

            item.setup?.let { setup ->
                Text(
                    text = setup,
                    fontWeight = FontWeight.Bold,
                    style = AndroidExampleTheme.typography.h4,
                    modifier = Modifier.padding(AndroidExampleTheme.paddings.padding_xs)
                )
            }

            item.delivery?.let { delivery ->
                Text(
                    text = delivery,
                    style = AndroidExampleTheme.typography.h5,
                    modifier = Modifier.padding(AndroidExampleTheme.paddings.padding_xs)
                )
            }
        }

        BackHandler(enabled = true, onBack = onClose)
    }
}

@Composable
@PreviewLightDark
private fun ItemDetailsViewPreview(
    @PreviewParameter(ItemDetailsViewPreviewParameterProvider::class) jokeDetailsUiModel: JokeDetailsUiModel
) {
    AndroidExampleTheme {
        ItemDetailsView(item = jokeDetailsUiModel, onClose = {})
    }
}

private class ItemDetailsViewPreviewParameterProvider :
    PreviewParameterProvider<JokeDetailsUiModel> {
    override val values = sequenceOf(
        JokeDetailsUiModel(
            joke = null,
            setup = "Setup",
            delivery = "Delivery",
        ),
        JokeDetailsUiModel(
            joke = "Joke",
            setup = null,
            delivery = null,
        ),
    )
}
