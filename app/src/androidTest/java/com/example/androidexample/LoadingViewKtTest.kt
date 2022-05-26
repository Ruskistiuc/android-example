package com.example.androidexample

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import com.example.androidexample.presentation.Loading
import com.example.androidexample.ui.theme.AndroidExampleTheme
import com.example.androidexample.util.LOADING_VIEW_LOADING_INDICATOR
import com.example.androidexample.util.assertScreenshotMatchesGolden
import org.junit.Rule
import org.junit.Test

class LoadingViewKtTest {

    @get: Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingView() {
        // Start with a paused clock
        composeTestRule.mainClock.autoAdvance = false

        composeTestRule.setContent {
            AndroidExampleTheme {
                Loading()
            }
        }

        // Advance clock (keeping it paused)
        composeTestRule.mainClock.advanceTimeBy(1000)

        composeTestRule
            .onNodeWithTag(LOADING_VIEW_LOADING_INDICATOR)
            .assertIsDisplayed()

        assertScreenshotMatchesGolden(
            goldenName = "loading_view_1000",
            node = composeTestRule.onRoot()
        )
    }
}
