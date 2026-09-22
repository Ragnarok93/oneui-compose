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
    fun progressRouteKeepsTheReferenceTwoGroupPresentation() {
        composeRule.onNodeWithTag("progress-indeterminate-group").assertIsDisplayed()
        composeRule.onNodeWithTag("progress-determinate-group").assertIsDisplayed()
    }

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
        composeRule.onNodeWithText("Open source licenses").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("GitHub").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Telegram").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Swipe up to expand").assertIsDisplayed()
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
        composeRule.onNodeWithTag("oneui-bottom-tab-overflow").performClick()
        composeRule.onNodeWithText("Nav item 13").assertIsDisplayed()
        composeRule.onNodeWithTag("bottom-nav-icons-overflow").assertIsDisplayed()
        composeRule.onNodeWithTag("oneui-bottom-navigation-overflow").performClick()
        composeRule.onNodeWithText("Item 6").assertIsDisplayed()
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

    @Test
    fun referenceWidgetInteractionsRemainComposeNative() {
        composeRule.onNodeWithContentDescription("Open navigation").performClick()
        composeRule.onNodeWithText(CatalogDestination.Widgets.label).performClick()

        composeRule.onNodeWithTag("oneui-spinner").performClick()
        composeRule.onNodeWithText("Item 4").performClick()
        composeRule.onNodeWithText("Item 4").assertIsDisplayed()

        composeRule.onNodeWithText("Open pop-up menu").performClick()
        composeRule.onNodeWithText("Pop-up menu item 4").assertIsDisplayed().performClick()

        composeRule.onNodeWithText("Enter action mode").performClick()
        composeRule.onNodeWithText("Select all").performClick()
        composeRule.onNodeWithText("3 selected").assertIsDisplayed()
    }

    @Test
    fun pickerAndAboutRoutesExposeReferenceStates() {
        composeRule.onNodeWithContentDescription("Open navigation").performClick()
        composeRule.onNodeWithText(CatalogDestination.Pickers.label).performClick()
        composeRule.onNodeWithTag("picker-number-triple").assertIsDisplayed()

        composeRule.onNodeWithTag("oneui-spinner").performClick()
        composeRule.onNodeWithText("TimePicker").performClick()
        composeRule.onNodeWithTag("picker-time-inline").assertIsDisplayed()

        composeRule.onNodeWithTag("oneui-spinner").performClick()
        composeRule.onNodeWithText("DatePicker").performClick()
        composeRule.onNodeWithTag("picker-date-inline").assertIsDisplayed()

        composeRule.onNodeWithTag("oneui-spinner").performClick()
        composeRule.onNodeWithText("SpinningDatePicker").performClick()
        composeRule.onNodeWithTag("picker-spinning-date").assertIsDisplayed()

        composeRule.onNodeWithTag("oneui-spinner").performClick()
        composeRule.onNodeWithText("SleepTimePicker").performClick()
        composeRule.onNodeWithTag("picker-sleep-time").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Open navigation").performClick()
        composeRule.onNodeWithText(CatalogDestination.About.label).performClick()
        composeRule.onNodeWithTag("about-status-button").performClick()
        composeRule.onNodeWithText("Failed").assertIsDisplayed()
    }
}
