package com.example.androidexample.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidexample.presentation.models.PresentationItemModel
import com.example.androidexample.ui.theme.AndroidExampleTheme
import com.example.androidexample.util.ITEM_DETAILS_VIEW_ITEM

@Composable
fun ItemDetailsView(item: PresentationItemModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AndroidExampleTheme.paddings.padding_l)
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
}

@Preview
@Composable
private fun ItemDetailsViewJokePreview() {
    AndroidExampleTheme {
        ItemDetailsView(
            item = PresentationItemModel(
                joke = "Joke",
                setup = null,
                delivery = null,
                onClick = { }
            )
        )
    }
}

@Preview
@Composable
private fun ItemDetailsViewSetupDeliveryPreview() {
    AndroidExampleTheme {
        ItemDetailsView(
            item = PresentationItemModel(
                joke = null,
                setup = "Setup",
                delivery = "Delivery",
                onClick = {}
            )
        )
    }
}
