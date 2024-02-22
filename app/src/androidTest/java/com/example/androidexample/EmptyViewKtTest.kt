package com.example.androidexample

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import com.example.androidexample.presentation.EMPTY_VIEW_TEXT
import com.example.androidexample.presentation.Empty
import com.example.androidexample.ui.theme.AndroidExampleTheme
import com.example.androidexample.util.assertScreenshotMatchesGolden
import org.junit.Rule
import org.junit.Test

class EmptyViewKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyView() {
        composeTestRule.setContent { AndroidExampleTheme { Empty() } }

        composeTestRule
            .onNodeWithTag(EMPTY_VIEW_TEXT)
            .assert(hasText("No data"))
            .assertIsDisplayed()

        assertScreenshotMatchesGolden(goldenName = "empty_view", node = composeTestRule.onRoot())
    }
}
