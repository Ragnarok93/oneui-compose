package org.oneui.compose.components

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import org.junit.Rule
import org.junit.Test
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.theme.OneUiTheme

class OneUiAccessibilityTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun actionableIcon_exposesContentDescriptionAndClickAction() {
        composeRule.setContent {
            OneUiTheme {
                OneUiIconButton(
                    icon = OneUiIcons.Search,
                    contentDescription = "Search",
                    onClick = {},
                )
            }
        }

        composeRule
            .onNodeWithContentDescription("Search")
            .assertHasClickAction()
    }
}
