package org.oneui.compose.demo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
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
import org.oneui.compose.demo.screens.StargazersOptionsTestTags

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
    fun actionModeActionsReportReferenceFeedbackAndExitSelection() {
        openStargazers()

        composeRule.onNodeWithText("Ada Lovelace").performTouchInput { longClick() }
        composeRule.onNodeWithTag("stargazer-action-message").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("1 contacts selected for Message").assertIsDisplayed()
        composeRule.onNodeWithTag("stargazer-action-mode").assertDoesNotExist()
    }

    @Test
    fun configuredCancelButtonExitsActionModeWithoutExecutingAnAction() {
        openStargazers()

        composeRule.onNodeWithContentDescription(StargazersOptionsTestTags.TriggerDescription).performClick()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ShowCancelButton).performClick()
        composeRule.onNodeWithText("Apply").performClick()

        composeRule.onNodeWithText("Ada Lovelace").performTouchInput { longClick() }
        composeRule.onNodeWithTag("stargazer-action-mode-cancel").assertIsDisplayed().performClick()
        composeRule.onNodeWithTag("stargazer-action-mode").assertDoesNotExist()
    }

    @Test
    fun refreshFailureRetainsRowsAndRetryRecovers() {
        openStargazers()

        composeRule.onNodeWithTag("stargazers-simulate-error").performClick()
        composeRule.waitUntil(timeoutMillis = 2_000) {
            runCatching {
                composeRule.onNodeWithTag("stargazers-refresh-error").assertIsDisplayed()
            }.isSuccess
        }
        composeRule.onNodeWithText("Ada Lovelace").assertIsDisplayed()
        composeRule.onNodeWithText("Retry").performClick()
        composeRule.waitUntil(timeoutMillis = 2_000) {
            runCatching {
                composeRule.onNodeWithTag("stargazers-refresh-error").assertDoesNotExist()
            }.isSuccess
        }
        composeRule.onNodeWithText("Ada Lovelace").assertIsDisplayed()
    }

    @Test
    fun optionsDialogStagesChangesUntilApply() {
        openStargazers()

        composeRule.onNodeWithContentDescription(StargazersOptionsTestTags.TriggerDescription)
            .assertIsDisplayed()
            .performClick()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.Dialog).assertIsDisplayed()
        composeRule.onNodeWithText("Stargazers options").assertIsDisplayed()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ShowIndexLetters).assertIsOff()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.AutoHideIndexScroll).assertIsOn()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ShowCancelButton).assertIsOff()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ActionModeDismiss).assertIsSelected()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ActionModeConcurrent).assertIsNotSelected()

        composeRule.onNodeWithTag(StargazersOptionsTestTags.ShowIndexLetters).performClick()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.AutoHideIndexScroll).performClick()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ShowCancelButton).performClick()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ActionModeConcurrent).performClick()
        composeRule.onNodeWithText("Cancel").performClick()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.Dialog).assertDoesNotExist()

        composeRule.onNodeWithContentDescription(StargazersOptionsTestTags.TriggerDescription).performClick()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ShowIndexLetters).assertIsOff()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.AutoHideIndexScroll).assertIsOn()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ShowCancelButton).assertIsOff()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ActionModeDismiss).assertIsSelected()

        composeRule.onNodeWithTag(StargazersOptionsTestTags.ShowIndexLetters).performClick()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.AutoHideIndexScroll).performClick()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ShowCancelButton).performClick()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ActionModeConcurrent).performClick()
        composeRule.onNodeWithText("Apply").performClick()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.Dialog).assertDoesNotExist()

        composeRule.onNodeWithContentDescription(StargazersOptionsTestTags.TriggerDescription).performClick()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ShowIndexLetters).assertIsOn()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.AutoHideIndexScroll).assertIsOff()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ShowCancelButton).assertIsOn()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ActionModeConcurrent).assertIsSelected()
        composeRule.onNodeWithTag(StargazersOptionsTestTags.ActionModeDismiss).assertIsNotSelected()
    }

    @Test
    fun profileExposesReferenceActionsDetailsAndQrSheet() {
        openStargazers()

        composeRule.onNodeWithText("Ada Lovelace").performClick()
        composeRule.onNodeWithTag("stargazer-profile").assertIsDisplayed()
        composeRule.onNodeWithTag("stargazer-profile-avatar").assertIsDisplayed()
        composeRule.onNodeWithText("London, UK").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Website").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Email").assertIsDisplayed()
        composeRule.onNodeWithTag("stargazer-profile-bottom-actions").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Share").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("QR code").assertIsDisplayed().performClick()
        composeRule.onNodeWithTag("stargazer-qr-sheet").assertIsDisplayed()
        composeRule.onNodeWithText("Scan this QR code on another device to view Ada Lovelace's profile.").assertIsDisplayed()
    }

    @Test
    fun stargazerFabShowsRepositoryTip() {
        openStargazers()

        composeRule.onNodeWithContentDescription("Star repositories")
            .assertIsDisplayed()
            .performClick()
        composeRule.onNodeWithTag("oneui-tip-popup").assertIsDisplayed()
        composeRule.onNodeWithText(
            "Star these github repositories:\n• OneUI Design lib\n• sesl-androidx\n• sesl-material.",
        )
            .assertIsDisplayed()
        composeRule.onNodeWithText("Dismiss").performClick()
        composeRule.onNodeWithTag("oneui-tip-popup").assertDoesNotExist()
    }

    @Test
    fun stargazerListShowsReferenceSwipeInstructionTip() {
        openStargazers()

        composeRule.waitUntil(timeoutMillis = 2_000) {
            composeRule.onAllNodesWithTag("oneui-tip-popup").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Swipe left to call, swipe right to message.").assertIsDisplayed()
    }
}
