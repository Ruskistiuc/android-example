package com.example.androidexample

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import com.example.androidexample.presentation.CONTENT_SCREEN_ITEMS_LIST
import com.example.androidexample.presentation.CONTENT_SCREEN_LIST_ITEM
import com.example.androidexample.presentation.EMPTY_VIEW_TEXT
import com.example.androidexample.presentation.ERROR_VIEW_RETRY_BUTTON
import com.example.androidexample.presentation.ITEM_DETAILS_VIEW_BACK_BUTTON
import com.example.androidexample.presentation.ITEM_DETAILS_VIEW_ITEM
import com.example.androidexample.presentation.LOADING_VIEW_LOADING_INDICATOR
import com.example.androidexample.presentation.MainScreen
import com.example.androidexample.presentation.models.ScreenState
import com.example.androidexample.presentation.models.ScreenState.Details.JokeDetailsUiModel
import com.example.androidexample.presentation.models.ScreenState.Loaded.JokeUiModel
import com.example.androidexample.ui.theme.AndroidExampleTheme
import com.example.androidexample.util.assertScreenshotMatchesGolden
import org.junit.Rule
import org.junit.Test

class MainScreenKtTest {

    private val items = listOf(
        JokeUiModel(joke = "Joke", setup = null, delivery = null, onClick = {}),
        JokeUiModel(joke = null, setup = "Setup", delivery = "Delivery", onClick = {}),
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainScreen_loading() {
        composeTestRule.mainClock.autoAdvance = false

        prepareScreen(ScreenState.Loading)

        composeTestRule.apply {
            mainClock.advanceTimeBy(1000)

            onNodeWithTag(LOADING_VIEW_LOADING_INDICATOR).assertIsDisplayed()
        }

        assertScreenshotMatchesGolden(
            goldenName = "main_screen_loading_1000",
            node = composeTestRule.onRoot(),
        )
    }

    @Test
    fun mainScreen_error() {
        prepareScreen(ScreenState.Error(onRetry = {}))

        composeTestRule
            .onNodeWithTag(ERROR_VIEW_RETRY_BUTTON)
            .assertIsDisplayed()

        assertScreenshotMatchesGolden(
            goldenName = "main_screen_error",
            node = composeTestRule.onRoot(),
        )
    }

    @Test
    fun mainScreen_empty() {
        prepareScreen(ScreenState.Empty)

        composeTestRule
            .onNodeWithTag(EMPTY_VIEW_TEXT)
            .assertIsDisplayed()

        assertScreenshotMatchesGolden(
            goldenName = "main_screen_empty",
            node = composeTestRule.onRoot(),
        )
    }

    @Test
    fun mainScreen_items() {
        prepareScreen(ScreenState.Loaded(items = items, onSwipeRefresh = {}))

        composeTestRule.apply {
            onNodeWithTag(CONTENT_SCREEN_ITEMS_LIST).assertIsDisplayed()

            onAllNodesWithTag(CONTENT_SCREEN_LIST_ITEM).assertCountEquals(2)
        }

        assertScreenshotMatchesGolden(
            goldenName = "main_screen_items",
            node = composeTestRule.onRoot(),
        )
    }

    @Test
    fun mainScreen_loading_and_items() {
        val screenState = mutableStateOf<ScreenState>(ScreenState.Loading)

        prepareMutableScreen(screenState)

        composeTestRule
            .onNodeWithTag(LOADING_VIEW_LOADING_INDICATOR)
            .assertIsDisplayed()

        screenState.value = ScreenState.Loaded(items = items, onSwipeRefresh = {})

        composeTestRule.apply {
            onNodeWithTag(LOADING_VIEW_LOADING_INDICATOR).assertDoesNotExist()

            onNodeWithTag(CONTENT_SCREEN_ITEMS_LIST).assertIsDisplayed()

            onAllNodesWithTag(CONTENT_SCREEN_LIST_ITEM).assertCountEquals(2)
        }
    }

    @Test
    fun mainScreen_error_retry_and_items() {
        val screenState = mutableStateOf<ScreenState>(ScreenState.Error(onRetry = {}))

        prepareMutableScreen(screenState)

        composeTestRule
            .onNodeWithTag(ERROR_VIEW_RETRY_BUTTON)
            .assertIsDisplayed()
            .performClick()

        screenState.value = ScreenState.Loaded(items = items, onSwipeRefresh = {})

        composeTestRule.apply {
            onNodeWithTag(ERROR_VIEW_RETRY_BUTTON).assertDoesNotExist()

            onNodeWithTag(CONTENT_SCREEN_ITEMS_LIST).assertIsDisplayed()

            onAllNodesWithTag(CONTENT_SCREEN_LIST_ITEM).assertCountEquals(2)
        }
    }

    @Test
    fun mainScreen_item_details_and_toolbar_back() {
        val screenState = mutableStateOf<ScreenState>(
            ScreenState.Loaded(items = items, onSwipeRefresh = {})
        )

        prepareMutableScreen(screenState)

        composeTestRule
            .onNodeWithText("Joke")
            .performClick()

        screenState.value = ScreenState.Details(
            item = JokeDetailsUiModel(joke = "Joke", setup = null, delivery = null),
            onClose = {},
        )

        composeTestRule.apply {
            onNodeWithTag(CONTENT_SCREEN_ITEMS_LIST).assertDoesNotExist()

            onNodeWithTag(ITEM_DETAILS_VIEW_ITEM).assert(hasAnyChild(hasText("Joke")))

            onNodeWithTag(ITEM_DETAILS_VIEW_BACK_BUTTON).performClick()

            screenState.value = ScreenState.Loaded(items = items, onSwipeRefresh = {})

            onNodeWithTag(CONTENT_SCREEN_ITEMS_LIST).assertIsDisplayed()

            onNodeWithTag(ITEM_DETAILS_VIEW_BACK_BUTTON).assertDoesNotExist()
        }
    }

    @Test
    fun mainScreen_item_details_and_physical_back() {
        val screenState = mutableStateOf<ScreenState>(
            ScreenState.Loaded(items = items, onSwipeRefresh = {})
        )

        prepareMutableScreen(screenState)

        composeTestRule
            .onNodeWithText("Setup")
            .performClick()

        screenState.value = ScreenState.Details(
            item = JokeDetailsUiModel(joke = null, setup = "Setup", delivery = "Delivery"),
            onClose = {},
        )

        composeTestRule.apply {
            onNodeWithTag(CONTENT_SCREEN_ITEMS_LIST).assertDoesNotExist()

            onNodeWithTag(ITEM_DETAILS_VIEW_ITEM)
                .assert(
                    hasAnyChild(hasText("Setup"))
                        .and(hasAnyChild(hasText("Delivery")))
                )

            Espresso.pressBack()

            screenState.value = ScreenState.Loaded(items = items, onSwipeRefresh = {})

            onNodeWithTag(CONTENT_SCREEN_ITEMS_LIST).assertIsDisplayed()

            onNodeWithTag(ITEM_DETAILS_VIEW_BACK_BUTTON).assertDoesNotExist()
        }
    }

    private fun prepareScreen(screenState: ScreenState) {
        composeTestRule.setContent {
            AndroidExampleTheme { MainScreen(screenState) }
        }
    }

    private fun prepareMutableScreen(screenState: MutableState<ScreenState>) {
        composeTestRule.setContent {
            AndroidExampleTheme { MainScreen(screenState.value) }
        }
    }
}
