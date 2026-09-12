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
import org.junit.Rule
import org.junit.Test
import org.oneui.compose.demo.screens.RecyclerViewCatalogTab

class IconsCatalogTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<CatalogActivity>()

    private fun openIcons() {
        composeRule.onNodeWithContentDescription("Open navigation").performClick()
        composeRule.onNodeWithText(CatalogDestination.RecyclerViews.label).performClick()
        composeRule.onNodeWithTag(CatalogDestination.RecyclerViews.testTag).assertIsDisplayed()
        composeRule.onNodeWithText(RecyclerViewCatalogTab.Icons.label).assertIsDisplayed()
        composeRule.onNodeWithTag(RecyclerViewCatalogTab.Icons.testTag).assertIsDisplayed()
    }

    @Test
    fun iconsTabSearchesTheReferenceDrawableNamespaceAndShowsEmptyState() {
        openIcons()

        composeRule.onNodeWithTag("icons-search-field").performTextInput("wifi secure")
        composeRule.onNodeWithText("ic_oui_wifi_secure").assertIsDisplayed()

        composeRule.onNodeWithTag("icons-search-field").performTextClearance()
        composeRule.onNodeWithTag("icons-search-field").performTextInput("definitely_not_an_icon")
        composeRule.onNodeWithText("No results found.").assertIsDisplayed()
    }

    @Test
    fun longPressEntersSelectionModeAndSelectAllCoversEveryDependencyDrawable() {
        openIcons()

        composeRule.onNodeWithText("ic_oui_add").performTouchInput { longClick() }
        composeRule.onNodeWithText("Select all").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("883 selected").assertIsDisplayed()
    }
}
