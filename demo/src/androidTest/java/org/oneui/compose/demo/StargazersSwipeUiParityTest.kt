package org.oneui.compose.demo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.oneui.compose.demo.screens.StargazersCatalogTab
import org.oneui.compose.theme.OneUiTheme

class StargazersSwipeUiParityTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setUp() {
        composeRule.setContent {
            OneUiTheme {
                StargazersCatalogTab()
            }
        }
    }

    @Test
    fun physicalSwipesExposeReferenceCallAndMessageFeedback() {
        waitForFirstRow()
        composeRule.onNodeWithTag("stargazer-row-1", useUnmergedTree = true).performTouchInput { swipeRight() }
        composeRule.onNodeWithTag("stargazer-swipe-feedback").assertIsDisplayed()
        composeRule.onNodeWithText("Calling Ada Lovelace...").assertIsDisplayed()
        composeRule.mainClock.advanceTimeBy(4_100L)
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("stargazer-row-2", useUnmergedTree = true).performTouchInput { swipeLeft() }
        composeRule.onNodeWithTag("stargazer-swipe-feedback").assertIsDisplayed()
        composeRule.onNodeWithText("Sending message to Alan Turing...").assertIsDisplayed()
    }

    @Test
    fun physicalSwipeMeaningDoesNotMirrorInRtl() {
        composeRule.setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                OneUiTheme {
                    StargazersCatalogTab()
                }
            }
        }

        waitForFirstRow()
        composeRule.onNodeWithTag("stargazer-row-1", useUnmergedTree = true)
            .performTouchInput { swipeRight() }
        composeRule.onNodeWithText("Calling Ada Lovelace...").assertIsDisplayed()
        composeRule.mainClock.advanceTimeBy(4_100L)
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("stargazer-row-2", useUnmergedTree = true)
            .performTouchInput { swipeLeft() }
        composeRule.onNodeWithText("Sending message to Alan Turing...").assertIsDisplayed()
    }

    private fun waitForFirstRow() {
        composeRule.waitUntil(timeoutMillis = 2_000) {
            composeRule.onAllNodesWithTag("stargazer-row-1", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }
}
