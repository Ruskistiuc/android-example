package com.example.androidexample

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import com.example.androidexample.presentation.ITEM_DETAILS_VIEW_BACK_BUTTON
import com.example.androidexample.presentation.ITEM_DETAILS_VIEW_ITEM
import com.example.androidexample.presentation.ItemDetailsView
import com.example.androidexample.presentation.models.ScreenState.Details.JokeDetailsUiModel
import com.example.androidexample.ui.theme.AndroidExampleTheme
import com.example.androidexample.util.assertScreenshotMatchesGolden
import org.junit.Rule
import org.junit.Test

class ItemDetailsViewKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun itemDetailsView_joke() {
        prepareScreen(joke = "Joke")

        composeTestRule.apply {
            onNodeWithTag(ITEM_DETAILS_VIEW_ITEM)
                .assert(hasAnyChild(hasText("Joke")))
                .assertIsDisplayed()

            onNodeWithTag(ITEM_DETAILS_VIEW_BACK_BUTTON)
                .assertHasClickAction()
                .assertIsDisplayed()
        }

        assertScreenshotMatchesGolden(
            goldenName = "item_details_view_joke",
            node = composeTestRule.onRoot(),
        )
    }

    @Test
    fun itemDetailsView_setup_delivery() {
        prepareScreen(setup = "Setup", delivery = "Delivery")

        composeTestRule.apply {
            onNodeWithTag(ITEM_DETAILS_VIEW_ITEM)
                .assert(
                    hasAnyChild(hasText("Setup"))
                        .and(hasAnyChild(hasText("Delivery")))
                )
                .assertIsDisplayed()

            onNodeWithTag(ITEM_DETAILS_VIEW_BACK_BUTTON)
                .assertHasClickAction()
                .assertIsDisplayed()
        }

        assertScreenshotMatchesGolden(
            goldenName = "item_details_view_setup_delivery",
            node = composeTestRule.onRoot(),
        )
    }

    private fun prepareScreen(
        joke: String? = null,
        setup: String? = null,
        delivery: String? = null,
    ) {
        composeTestRule.setContent {
            AndroidExampleTheme {
                ItemDetailsView(
                    item = JokeDetailsUiModel(joke = joke, setup = setup, delivery = delivery),
                    onClose = {},
                )
            }
        }
    }
}
