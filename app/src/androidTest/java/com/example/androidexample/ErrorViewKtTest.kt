package com.example.androidexample

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import com.example.androidexample.presentation.ERROR_VIEW_RETRY_BUTTON
import com.example.androidexample.presentation.Error
import com.example.androidexample.ui.theme.AndroidExampleTheme
import com.example.androidexample.util.assertScreenshotMatchesGolden
import org.junit.Rule
import org.junit.Test

class ErrorViewKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorView() {
        composeTestRule.setContent { AndroidExampleTheme { Error(onClickRetry = {}) } }

        composeTestRule
            .onNodeWithTag(ERROR_VIEW_RETRY_BUTTON)
            .assert(hasText("Retry").and(hasClickAction()))
            .assertIsDisplayed()

        assertScreenshotMatchesGolden(goldenName = "error_view", node = composeTestRule.onRoot())
    }
}
