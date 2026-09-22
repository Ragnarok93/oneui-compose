package org.oneui.compose.demo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class CatalogParitySmokeTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<CatalogActivity>()

    @Test
    fun preferenceAboutAndCustomAboutRoutesAreRealComposeSurfaces() {
        composeRule.onNodeWithContentDescription("Preferences").performClick()
        composeRule.onNodeWithTag("catalog-preferences").assertIsDisplayed()
        composeRule.onNodeWithText("Suggestion").assertIsDisplayed()

        composeRule.onNodeWithText("About Sample App").performClick()
        composeRule.onNodeWithTag("oneui-app-info").assertIsDisplayed()
        composeRule.onNodeWithText("OneUI Compose").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Open navigation").performClick()
        composeRule.onNodeWithText(CatalogDestination.CustomAbout.label).performClick()
        composeRule.onNodeWithTag("oneui-custom-about").assertIsDisplayed()
        composeRule.onNodeWithText("Yanndroid").assertIsDisplayed()
    }

    @Test
    fun navigationAndWidgetReferenceItemsAreReachable() {
        composeRule.onNodeWithContentDescription("Open navigation").performClick()
        composeRule.onNodeWithText(CatalogDestination.Navigation.label).performClick()
        composeRule.onNodeWithTag("tabs-rounded-text").assertIsDisplayed()
        composeRule.onNodeWithTag("tabs-sub-scrollable").assertIsDisplayed()
        composeRule.onNodeWithTag("bottom-nav-icons-overflow").assertIsDisplayed()
        composeRule.onNodeWithTag("bottom-nav-text").assertIsDisplayed()
        composeRule.onNodeWithTag("bottom-tabs-13-items").assertIsDisplayed()
        composeRule.onNodeWithTag("navigation-rail-control").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Open navigation").performClick()
        composeRule.onNodeWithText(CatalogDestination.Widgets.label).performClick()
        composeRule.onNodeWithTag("widget-spinner").assertIsDisplayed()
        composeRule.onNodeWithTag("widget-seven-button-styles").assertIsDisplayed()
        composeRule.onNodeWithTag("widget-search-view").assertIsDisplayed()
        composeRule.onNodeWithTag("widget-card-switch-radio-rows").assertIsDisplayed()
        composeRule.onNodeWithTag("widget-relative-links").assertIsDisplayed()
        composeRule.onNodeWithTag("widget-bottom-tip").assertIsDisplayed()
        composeRule.onNodeWithTag("widget-switch-bar-progress").assertIsDisplayed()
        composeRule.onNodeWithTag("widget-suggest-appbar").assertIsDisplayed()
        composeRule.onNodeWithTag("catalog-popup-menu").assertIsDisplayed()
        composeRule.onNodeWithTag("catalog-search-mode").assertIsDisplayed()
        composeRule.onNodeWithTag("catalog-action-mode").assertIsDisplayed()
    }
}
