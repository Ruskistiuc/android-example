package com.example.androidexample

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import com.example.androidexample.presentation.ERROR_VIEW_RETRY_BUTTON
import com.example.androidexample.presentation.ITEM_DETAILS_VIEW_BACK_BUTTON
import com.example.androidexample.presentation.ITEM_DETAILS_VIEW_ITEM
import com.example.androidexample.presentation.LOADING_VIEW_LOADING_INDICATOR
import com.example.androidexample.presentation.MAIN_SCREEN_ITEMS_LIST
import com.example.androidexample.presentation.MAIN_SCREEN_LIST_ITEM
import com.example.androidexample.presentation.MainScreen
import com.example.androidexample.presentation.models.ScreenState
import com.example.androidexample.presentation.models.ScreenState.Details.JokeDetailsUiModel
import com.example.androidexample.presentation.models.ScreenState.Loaded.JokeUiModel
import com.example.androidexample.ui.theme.AndroidExampleTheme
import org.junit.Rule
import org.junit.Test

class MainScreenKtTest {

    private val itemsList = listOf(
        JokeUiModel(
            joke = "Joke",
            setup = null,
            delivery = null,
            onClick = {}
        ),
        JokeUiModel(
            joke = null,
            setup = "Setup",
            delivery = "Delivery",
            onClick = {}
        )
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainScreen_loading() {
        composeTestRule.mainClock.autoAdvance = false

        composeTestRule.setContent {
            AndroidExampleTheme {
                MainScreen(ScreenState.Loading)
            }
        }

        composeTestRule.apply {
            mainClock.advanceTimeBy(1000)

            onNodeWithTag(LOADING_VIEW_LOADING_INDICATOR)
                .assertIsDisplayed()
        }

//        assertScreenshotMatchesGolden(
//            goldenName = "main_screen_loading_1000",
//            node = composeTestRule.onRoot()
//        )
    }

    @Test
    fun mainScreen_error() {
        composeTestRule.setContent {
            AndroidExampleTheme {
                MainScreen(ScreenState.Error(onRetry = {}))
            }
        }

        composeTestRule
            .onNodeWithTag(ERROR_VIEW_RETRY_BUTTON)
            .assertIsDisplayed()

//        assertScreenshotMatchesGolden(
//            goldenName = "main_screen_error",
//            node = composeTestRule.onRoot()
//        )
    }

    @Test
    fun mainScreen_items() {
        composeTestRule.setContent {
            AndroidExampleTheme {
                MainScreen(ScreenState.Loaded(items = itemsList, onSwipeRefresh = {}))
            }
        }

        composeTestRule.apply {
            onNodeWithTag(MAIN_SCREEN_ITEMS_LIST)
                .assertIsDisplayed()

            onAllNodesWithTag(MAIN_SCREEN_LIST_ITEM)
                .assertCountEquals(2)
                .assertAll(hasClickAction())

            onNodeWithText("Joke").assertIsDisplayed()

            onNodeWithText("Setup").assertIsDisplayed()
            onNodeWithText("Delivery").assertIsDisplayed()
        }

//        assertScreenshotMatchesGolden(
//            goldenName = "main_screen_items",
//            node = composeTestRule.onRoot()
//        )
    }

    @Test
    fun mainScreen_loading_and_items() {
        // States that can cause recompositions
        val screenState = mutableStateOf<ScreenState>(ScreenState.Loading)

        composeTestRule.setContent {
            AndroidExampleTheme {
                MainScreen(screenState.value)
            }
        }

        composeTestRule
            .onNodeWithTag(LOADING_VIEW_LOADING_INDICATOR)
            .assertIsDisplayed()

        // The states are changed, but there is no recomposition
        screenState.value = ScreenState.Loaded(items = itemsList, onSwipeRefresh = {})

        // Assertions triggers recomposition
        composeTestRule.apply {
            onNodeWithTag(LOADING_VIEW_LOADING_INDICATOR)
                .assertDoesNotExist()

            onNodeWithTag(MAIN_SCREEN_ITEMS_LIST)
                .assertIsDisplayed()

            onAllNodesWithTag(MAIN_SCREEN_LIST_ITEM)
                .assertCountEquals(2)
        }
    }

    @Test
    fun mainScreen_error_retry_and_items() {
        // States that can cause recompositions
        val screenState = mutableStateOf<ScreenState>(ScreenState.Error(onRetry = {}))

        composeTestRule.setContent {
            AndroidExampleTheme {
                MainScreen(screenState.value)
            }
        }

        composeTestRule
            .onNodeWithTag(ERROR_VIEW_RETRY_BUTTON)
            .assertIsDisplayed()
            .performClick()

        // The states are changed, but there is no recomposition
        screenState.value = ScreenState.Loaded(items = itemsList, onSwipeRefresh = {})

        // Assertions triggers recomposition
        composeTestRule.apply {
            onNodeWithTag(ERROR_VIEW_RETRY_BUTTON)
                .assertDoesNotExist()

            onNodeWithTag(MAIN_SCREEN_ITEMS_LIST)
                .assertIsDisplayed()

            onAllNodesWithTag(MAIN_SCREEN_LIST_ITEM)
                .assertCountEquals(2)
        }
    }

    @Test
    fun mainScreen_item_details_and_toolbar_back() {
        val screenState = mutableStateOf<ScreenState>(
            ScreenState.Loaded(items = itemsList, onSwipeRefresh = {})
        )

        composeTestRule.setContent {
            AndroidExampleTheme {
                MainScreen(screenState.value)
            }
        }

        composeTestRule
            .onNodeWithText("Joke")
            .performClick()

        screenState.value = ScreenState.Details(
            item = JokeDetailsUiModel(joke = "Joke", setup = null, delivery = null),
            onClose = {},
        )

        composeTestRule.apply {
            // Open item details
            onNodeWithTag(MAIN_SCREEN_ITEMS_LIST)
                .assertDoesNotExist()

            onNodeWithTag(ITEM_DETAILS_VIEW_ITEM)
                .assert(
                    hasAnyChild(
                        hasText("Joke")
                    )
                )

            // close item details using toolbar
            onNodeWithTag(ITEM_DETAILS_VIEW_BACK_BUTTON)
                .performClick()

            screenState.value = ScreenState.Loaded(items = itemsList, onSwipeRefresh = {})

            onNodeWithTag(MAIN_SCREEN_ITEMS_LIST)
                .assertIsDisplayed()

            onNodeWithTag(ITEM_DETAILS_VIEW_BACK_BUTTON)
                .assertDoesNotExist()
        }
    }

    @Test
    fun mainScreen_item_details_and_physical_back() {
        val screenState = mutableStateOf<ScreenState>(
            ScreenState.Loaded(items = itemsList, onSwipeRefresh = {})
        )
        composeTestRule.setContent {
            AndroidExampleTheme {
                MainScreen(screenState.value)
            }
        }

        composeTestRule
            .onNodeWithText("Setup")
            .performClick()

        screenState.value = ScreenState.Details(
            item = JokeDetailsUiModel(joke = null, setup = "Setup", delivery = "Delivery"),
            onClose = {},
        )

        composeTestRule.apply {
            // Open item details
            onNodeWithTag(MAIN_SCREEN_ITEMS_LIST)
                .assertDoesNotExist()

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

            // close item details using physical back
            Espresso.pressBack()

            screenState.value = ScreenState.Loaded(items = itemsList, onSwipeRefresh = {})

            onNodeWithTag(MAIN_SCREEN_ITEMS_LIST)
                .assertIsDisplayed()

            onNodeWithTag(ITEM_DETAILS_VIEW_BACK_BUTTON)
                .assertDoesNotExist()
        }
    }
}
