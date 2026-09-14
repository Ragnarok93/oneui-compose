package org.oneui.compose.demo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
import org.oneui.compose.demo.screens.RecyclerViewCatalogTab

class StargazersSwipeUiParityTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<CatalogActivity>()

    private fun openStargazers() {
        composeRule.onNodeWithContentDescription("Open navigation").performClick()
        composeRule.onNodeWithText(CatalogDestination.RecyclerViews.label).performClick()
        composeRule.onNodeWithText(RecyclerViewCatalogTab.Stargazers.label).performClick()
        composeRule.onNodeWithTag(RecyclerViewCatalogTab.Stargazers.testTag).assertIsDisplayed()
    }

    @Test
    fun physicalSwipesExposeReferenceCallAndMessageFeedback() {
        openStargazers()

        composeRule.onNodeWithTag("stargazer-row-1").performTouchInput { swipeRight() }
        composeRule.onNodeWithTag("stargazer-swipe-feedback").assertIsDisplayed()
        composeRule.onNodeWithText("Calling Ada Lovelace...").assertIsDisplayed()

        composeRule.onNodeWithTag("stargazer-row-2").performTouchInput { swipeLeft() }
        composeRule.onNodeWithTag("stargazer-swipe-feedback").assertIsDisplayed()
        composeRule.onNodeWithText("Sending message to Alan Turing...").assertIsDisplayed()
    }
}
