package com.example.androidexample.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidexample.presentation.models.JokeUiModel
import com.example.androidexample.presentation.models.MainUiModel
import com.example.androidexample.ui.theme.AndroidExampleTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

const val MAIN_SCREEN_ITEMS_LIST = "itemsList"
const val MAIN_SCREEN_LIST_ITEM = "listItem"

@Composable
fun MainScreen(uiModel: MainUiModel) {
    val listState = rememberLazyListState()

    Surface {
        when {
            uiModel.loading -> Loading()

            uiModel.error -> Error(onClickRetry = uiModel.onClickRetry)

            uiModel.selected != null -> ItemDetailsView(
                item = uiModel.selected,
                uiModel.onCloseItemDetails
            )

            else -> SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = false),
                onRefresh = uiModel.onSwipeRefresh
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(
                        all = AndroidExampleTheme.paddings.padding_l
                    ),
                    verticalArrangement = Arrangement.spacedBy(
                        space = AndroidExampleTheme.paddings.padding_l
                    ),
                    modifier = Modifier.testTag(MAIN_SCREEN_ITEMS_LIST)
                ) {
                    items(items = uiModel.items) { item ->
                        ListItem(
                            item = item,
                            selectItem = item.onClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ListItem(
    item: JokeUiModel,
    selectItem: (JokeUiModel) -> Unit
) {
    Card(
        elevation = 3.dp,
        shape = AndroidExampleTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { selectItem(item) }
                .padding(AndroidExampleTheme.paddings.padding_m)
                .testTag(MAIN_SCREEN_LIST_ITEM)
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
@CombinedPreviews
fun ListItemSetupDeliveryPreview() {
    AndroidExampleTheme {
        ListItem(
            item = JokeUiModel(
                joke = null, setup = "Text 1", delivery = "Text 2",
                onClick = {}
            ),
            selectItem = {}
        )
    }
}

@Composable
@CombinedPreviews
fun ListItemJokePreview() {
    AndroidExampleTheme {
        ListItem(
            item = JokeUiModel(
                joke = "Joke 1", setup = null, delivery = null,
                onClick = {}
            ),
            selectItem = {}
        )
    }
}
