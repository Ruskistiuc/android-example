package com.example.androidexample

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.androidexample.presentation.ERROR_VIEW_RETRY_BUTTON
import com.example.androidexample.presentation.Error
import com.example.androidexample.ui.theme.AndroidExampleTheme
import org.junit.Rule
import org.junit.Test

class ErrorViewKtTest {

    /**
     * This rule lets you set the Compose content under test and interact with it.
     *
     * Finding UI elements, checking their properties and performing actions is done
     * through the test rule, following this pattern:
     * -> composeTestRule{.finder}{.assertion}{.action}
     */
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorView() {
        composeTestRule.setContent {
            AndroidExampleTheme {
                Error(onClickRetry = {})
            }
        }

        /**
         * Print the Semantics tree of the Error View
         *
         * useUnmergedTree = true - get the complete tree
         * useUnmergedTree = false - get simplified tree
         */
        composeTestRule.onRoot(useUnmergedTree = true)
            .printToLog("ErrorViewSemanticsTree")

        /**
         * It's best to use the Test Tag matches were possible
         * as these can be controlled in code and modified for our needs.
         */
        composeTestRule
            .onNodeWithTag(ERROR_VIEW_RETRY_BUTTON)
            .assert(
                hasText("Retry").and(
                    hasClickAction()
                )
            )
            .assertIsDisplayed()

//        assertScreenshotMatchesGolden(
//            goldenName = "error_view",
//            node = composeTestRule.onRoot()
//        )
    }
}
