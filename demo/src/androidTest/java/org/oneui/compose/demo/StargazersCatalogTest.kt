package org.oneui.compose.demo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.oneui.compose.demo.screens.RecyclerViewCatalogTab
import org.oneui.compose.demo.screens.StargazersCatalogSamples

class StargazersCatalogTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<CatalogActivity>()

    private fun openStargazers() {
        composeRule.onNodeWithContentDescription("Open navigation").performClick()
        composeRule.onNodeWithText(CatalogDestination.RecyclerViews.label).performClick()
        composeRule.onNodeWithText(RecyclerViewCatalogTab.Stargazers.label).performClick()
        composeRule.onNodeWithTag(RecyclerViewCatalogTab.Stargazers.testTag).assertIsDisplayed()
    }

    @Test
    fun sampleSetIsDeterministicAndSearchMatchesReferenceHint() {
        assertEquals(8, StargazersCatalogSamples.size)
        openStargazers()

        composeRule.onNodeWithContentDescription("Scroll to A").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Scroll to G").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Scroll to M").assertIsDisplayed()

        composeRule.onNodeWithTag("stargazers-search-field").performTextInput("Ada")
        composeRule.onNodeWithText("Ada Lovelace").assertIsDisplayed()

        composeRule.onNodeWithTag("stargazers-search-field").performTextClearance()
        composeRule.onNodeWithTag("stargazers-search-field").performTextInput("definitely_not_a_contact")
        composeRule.onNodeWithText("No results found.").assertIsDisplayed()
    }

    @Test
    fun longPressEntersActionModeAndSelectAllCoversProfiles() {
        openStargazers()

        composeRule.onNodeWithText("Ada Lovelace").performTouchInput { longClick() }
        composeRule.onNodeWithText("1 selected").assertIsDisplayed()
        composeRule.onNodeWithText("Select all").performClick()
        composeRule.onNodeWithText("8 selected").assertIsDisplayed()
    }

    @Test
    fun profileExposesDetailsShareAndQrSheet() {
        openStargazers()

        composeRule.onNodeWithText("Ada Lovelace").performClick()
        composeRule.onNodeWithTag("stargazer-profile").assertIsDisplayed()
        composeRule.onNodeWithTag("stargazer-profile-avatar").assertIsDisplayed()
        composeRule.onNodeWithText("London, UK").assertIsDisplayed()
        composeRule.onNodeWithText("Share").assertIsDisplayed()
        composeRule.onNodeWithText("QR code").performClick()
        composeRule.onNodeWithTag("stargazer-qr-sheet").assertIsDisplayed()
        composeRule.onNodeWithText("Scan this QR code on another device to view Ada Lovelace's profile.").assertIsDisplayed()
    }
}
