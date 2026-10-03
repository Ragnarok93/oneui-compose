package org.oneui.compose.demo

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.oneui.compose.patterns.shell.OneUiLayoutMode
import org.oneui.compose.theme.OneUiTheme

class CatalogDesktopModeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun forcedDesktopCatalogKeepsSharedNavigationContent() {
        composeRule.setContent {
            OneUiTheme {
                CatalogApp(
                    onOpenLegacyShowcase = {},
                    layoutMode = OneUiLayoutMode.Desktop,
                )
            }
        }

        composeRule.onNodeWithTag("oneui-shell-desktop").assertIsDisplayed()
        composeRule.onNodeWithTag("oneui-shell-desktop-navigation").assertIsDisplayed()
        composeRule.onNodeWithTag("oneui-shell-desktop-content").assertIsDisplayed()
        composeRule.onNodeWithTag("catalog-desktop-context-bar").assertIsDisplayed()

        composeRule.onNodeWithText(CatalogDestination.Navigation.label).performClick()

        composeRule.onNodeWithTag("tabs-rounded-text").assertIsDisplayed()
        composeRule.onNodeWithTag("navigation-rail-control").assertIsDisplayed()
        composeRule.onAllNodesWithContentDescription("Open navigation").assertCountEquals(0)
    }
}
