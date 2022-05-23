package com.example.androidexample.presentation

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidexample.presentation.models.PresentationItemModel
import com.example.androidexample.presentation.models.PresentationModel
import com.example.androidexample.ui.theme.AndroidExampleTheme

@Composable
fun MainScreen(model: PresentationModel) {
    val listState = rememberLazyListState()

    Surface {
        when {
            model.isLoading -> {
                Loading()
            }
            model.isError -> {
                // TODO(Implement retry mechanism)
            }
            else -> {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(
                        all = AndroidExampleTheme.paddings.padding_l
                    ),
                    verticalArrangement = Arrangement.spacedBy(
                        space = AndroidExampleTheme.paddings.padding_l
                    )
                ) {
                    items(items = model.items) { i ->
                        ListItem(i)
                    }
                }
            }
        }
    }
}

@Composable
private fun ListItem(item: PresentationItemModel) {
    Card(
        elevation = 3.dp,
        shape = AndroidExampleTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AndroidExampleTheme.paddings.padding_m)
        ) {
            Text(
                text = item.setup,
                fontWeight = FontWeight.Bold,
                style = AndroidExampleTheme.typography.subtitle1,
                modifier = Modifier.padding(AndroidExampleTheme.paddings.padding_xs)
            )

            Text(
                text = item.punchline,
                style = AndroidExampleTheme.typography.body1,
                modifier = Modifier.padding(AndroidExampleTheme.paddings.padding_xs)
            )
        }
    }
}

@Preview
@Composable
fun ListItemPreview() {
    AndroidExampleTheme {
        ListItem(
            item = PresentationItemModel(
                setup = "Text 1", punchline = "Text 2"
            )
        )
    }
}
