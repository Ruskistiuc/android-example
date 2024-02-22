package com.example.androidexample

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import com.example.androidexample.presentation.CONTENT_SCREEN_ITEMS_LIST
import com.example.androidexample.presentation.CONTENT_SCREEN_LIST_ITEM
import com.example.androidexample.presentation.MainScreen
import com.example.androidexample.presentation.models.ScreenState
import com.example.androidexample.ui.theme.AndroidExampleTheme
import com.example.androidexample.util.assertScreenshotMatchesGolden
import org.junit.Rule
import org.junit.Test

class ContentViewKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun contentScreen_items() {
        val items = listOf(
            ScreenState.Loaded.JokeUiModel(
                joke = "Joke",
                setup = null,
                delivery = null,
                onClick = {},
            ),
            ScreenState.Loaded.JokeUiModel(
                joke = null,
                setup = "Setup",
                delivery = "Delivery",
                onClick = {},
            ),
        )

        composeTestRule.setContent {
            AndroidExampleTheme {
                MainScreen(ScreenState.Loaded(items = items, onSwipeRefresh = {}))
            }
        }

        composeTestRule.apply {
            onNodeWithTag(CONTENT_SCREEN_ITEMS_LIST).assertIsDisplayed()

            onAllNodesWithTag(CONTENT_SCREEN_LIST_ITEM)
                .assertCountEquals(2)
                .assertAll(hasClickAction())

            onNodeWithText("Joke").assertIsDisplayed()

            onNodeWithText("Setup").assertIsDisplayed()
            onNodeWithText("Delivery").assertIsDisplayed()
        }

        assertScreenshotMatchesGolden(
            goldenName = "content_screen_items",
            node = composeTestRule.onRoot(),
        )
    }
}
