package com.example.androidexample

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.androidexample.presentation.ITEM_DETAILS_VIEW_BACK_BUTTON
import com.example.androidexample.presentation.ITEM_DETAILS_VIEW_ITEM
import com.example.androidexample.presentation.ItemDetailsView
import com.example.androidexample.presentation.models.ScreenState.Details.JokeDetailsUiModel
import com.example.androidexample.ui.theme.AndroidExampleTheme
import org.junit.Rule
import org.junit.Test

class ItemDetailsViewKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun itemDetailsView_joke() {
        composeTestRule.setContent {
            AndroidExampleTheme {
                ItemDetailsView(
                    item = JokeDetailsUiModel(
                        joke = "Joke",
                        setup = null,
                        delivery = null,
                    ),
                    onClose = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithTag(ITEM_DETAILS_VIEW_ITEM)
                .assert(
                    hasAnyChild(
                        hasText("Joke")
                    )
                )
                .assertIsDisplayed()

            onNodeWithTag(ITEM_DETAILS_VIEW_BACK_BUTTON)
                .assertHasClickAction()
                .assertIsDisplayed()
        }

//        assertScreenshotMatchesGolden(
//            goldenName = "item_details_view_joke",
//            node = composeTestRule.onRoot()
//        )
    }

    @Test
    fun itemDetailsView_setup_delivery() {
        composeTestRule.setContent {
            AndroidExampleTheme {
                ItemDetailsView(
                    item = JokeDetailsUiModel(
                        joke = null,
                        setup = "Setup",
                        delivery = "Delivery",
                    ),
                    onClose = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithTag(ITEM_DETAILS_VIEW_ITEM)
                .assert(
                    hasAnyChild(
                        hasText("Setup")
                    ).and(
                        hasAnyChild(
                            hasText("Delivery")
                        )
                    )
                )
                .assertIsDisplayed()

            onNodeWithTag(ITEM_DETAILS_VIEW_BACK_BUTTON)
                .assertHasClickAction()
                .assertIsDisplayed()
        }

//        assertScreenshotMatchesGolden(
//            goldenName = "item_details_view_setup_delivery",
//            node = composeTestRule.onRoot()
//        )
    }
}
