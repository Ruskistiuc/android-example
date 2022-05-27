package com.example.androidexample

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import com.example.androidexample.presentation.ItemDetailsView
import com.example.androidexample.presentation.models.PresentationItemModel
import com.example.androidexample.ui.theme.AndroidExampleTheme
import com.example.androidexample.util.ITEM_DETAILS_VIEW_ITEM
import com.example.androidexample.util.assertScreenshotMatchesGolden
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
                    item = PresentationItemModel(
                        joke = "Joke",
                        setup = null,
                        delivery = null,
                        onClick = {}
                    )
                )
            }
        }

        composeTestRule
            .onNodeWithTag(ITEM_DETAILS_VIEW_ITEM)
            .assert(
                hasAnyChild(
                    hasText("Joke")
                )
            )
            .assertIsDisplayed()

        assertScreenshotMatchesGolden(
            goldenName = "item_details_view_joke",
            node = composeTestRule.onRoot()
        )
    }

    @Test
    fun itemDetailsView_setup_delivery() {
        composeTestRule.setContent {
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

        composeTestRule
            .onNodeWithTag(ITEM_DETAILS_VIEW_ITEM)
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

        assertScreenshotMatchesGolden(
            goldenName = "item_details_view_setup_delivery",
            node = composeTestRule.onRoot()
        )
    }
}
