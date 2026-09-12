package org.oneui.compose.demo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.oneui.compose.demo.screens.AppsCatalogSamples
import org.oneui.compose.demo.screens.RecyclerViewCatalogTab
import org.oneui.compose.patterns.apppicker.OneUiAppPickerListType

class AppsCatalogTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<CatalogActivity>()

    private fun openApps() {
        composeRule.onNodeWithContentDescription("Open navigation").performClick()
        composeRule.onNodeWithText(CatalogDestination.RecyclerViews.label).performClick()
        composeRule.onNodeWithText(RecyclerViewCatalogTab.Apps.label).performClick()
        composeRule.onNodeWithTag(RecyclerViewCatalogTab.Apps.testTag).assertIsDisplayed()
    }

    @Test
    fun exposesSevenReferenceTypesAndDeterministicFixtures() {
        assertEquals(7, OneUiAppPickerListType.entries.size)
        assertEquals(8, AppsCatalogSamples.size)
        openApps()

        composeRule.onNodeWithContentDescription("Change app picker type").performClick()
        composeRule.onNodeWithText("Grid + checkbox").performClick()
        composeRule.onNodeWithTag("apps-grid").assertIsDisplayed()
    }

    @Test
    fun searchFiltersByAppLabelAndPackage() {
        openApps()

        composeRule.onNodeWithTag("apps-search-field").performTextInput("Calendar")
        composeRule.onNodeWithText("Calendar").assertIsDisplayed()

        composeRule.onNodeWithTag("apps-search-field").performTextClearance()
        composeRule.onNodeWithTag("apps-search-field").performTextInput("not.a.real.package")
        composeRule.onNodeWithText("No apps found.").assertIsDisplayed()
    }

    @Test
    fun selectLayoutModeTracksSelectedApps() {
        openApps()

        composeRule.onNodeWithText("Select layout mode").performClick()
        composeRule.onNodeWithTag("apps-selected-panel").assertIsDisplayed()
        composeRule.onNodeWithText("Calendar").performClick()
        composeRule.onNodeWithText("1 selected").assertIsDisplayed()
    }
}
