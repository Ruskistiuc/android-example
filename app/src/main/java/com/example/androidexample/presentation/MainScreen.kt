package com.example.androidexample.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.androidexample.presentation.models.ScreenState
import com.example.androidexample.presentation.models.ScreenState.Loaded.JokeUiModel
import com.example.androidexample.ui.theme.AndroidExampleTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

const val MAIN_SCREEN_ITEMS_LIST = "itemsList"
const val MAIN_SCREEN_LIST_ITEM = "listItem"

@Composable
fun MainScreen(screenState: ScreenState) {
    Surface {
        when (screenState) {
            ScreenState.Loading -> Loading()

            is ScreenState.Error -> Error(screenState.onRetry)

            is ScreenState.Details -> ItemDetailsView(screenState.item, screenState.onClose)

            ScreenState.Empty -> Empty()

            is ScreenState.Loaded -> SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = false),
                onRefresh = screenState.onSwipeRefresh,
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(AndroidExampleTheme.paddings.padding_l),
                    verticalArrangement = Arrangement.spacedBy(AndroidExampleTheme.paddings.padding_l),
                    modifier = Modifier.testTag(MAIN_SCREEN_ITEMS_LIST),
                    content = { items(items = screenState.items) { ListItem(item = it) } }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ListItem(item: JokeUiModel) {
    Card(
        onClick = item.onClick,
        shape = AndroidExampleTheme.shapes.medium,
        modifier = Modifier.testTag(MAIN_SCREEN_LIST_ITEM)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AndroidExampleTheme.paddings.padding_m)
        ) {
            item.joke?.let { joke ->
                Text(
                    text = joke,
                    fontWeight = FontWeight.Bold,
                    style = AndroidExampleTheme.typography.subtitle1,
                    modifier = Modifier.padding(AndroidExampleTheme.paddings.padding_xs)
                )
            }

            item.setup?.let { setup ->
                Text(
                    text = setup,
                    fontWeight = FontWeight.Bold,
                    style = AndroidExampleTheme.typography.subtitle1,
                    modifier = Modifier.padding(AndroidExampleTheme.paddings.padding_xs)
                )
            }

            item.delivery?.let { delivery ->
                Text(
                    text = delivery,
                    style = AndroidExampleTheme.typography.body1,
                    modifier = Modifier.padding(AndroidExampleTheme.paddings.padding_xs)
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
fun ListItemPreview(
    @PreviewParameter(ListItemPreviewParameterProvider::class) jokeUiModel: JokeUiModel
) {
    AndroidExampleTheme {
        ListItem(item = jokeUiModel)
    }
}

private class ListItemPreviewParameterProvider : PreviewParameterProvider<JokeUiModel> {
    override val values = sequenceOf(
        JokeUiModel(
            joke = null,
            setup = "Setup",
            delivery = "Delivery",
            onClick = {}
        ),
        JokeUiModel(
            joke = "Joke",
            setup = null,
            delivery = null,
            onClick = {}
        )
    )
}
