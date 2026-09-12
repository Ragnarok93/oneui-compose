package org.oneui.compose.demo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class CatalogSmokeTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<CatalogActivity>()

    @Test
    fun everyDrawerDestinationOpens() {
        CatalogDestination.drawerEntries
            .filterNot { it == CatalogDestination.LegacyShowcase }
            .forEachIndexed { index, destination ->
                if (index > 0) {
                    composeRule.onNodeWithContentDescription("Open navigation").performClick()
                    composeRule.onNodeWithText(destination.label).performClick()
                }
                composeRule.onNodeWithTag(destination.testTag).assertIsDisplayed()
            }
    }

    @Test
    fun preferencesActionOpensPreferencesRoute() {
        composeRule.onNodeWithContentDescription("Preferences").performClick()
        composeRule.onNodeWithTag(CatalogDestination.Preferences.testTag).assertIsDisplayed()
    }

    @Test
    fun parityActionOpensStatusRoute() {
        composeRule.onNodeWithContentDescription("Parity status").performClick()
        composeRule.onNodeWithTag(CatalogDestination.ParityStatus.testTag).assertIsDisplayed()
    }
}
