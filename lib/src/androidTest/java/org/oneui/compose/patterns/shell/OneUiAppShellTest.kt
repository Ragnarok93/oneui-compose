package org.oneui.compose.patterns.shell

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.LayoutDirection
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.theme.OneUiTheme

class OneUiAppShellTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun desktopOverrideRendersPermanentPaneAndContextBar() {
        setShellContent(layoutMode = OneUiLayoutMode.Desktop)

        composeRule.onNodeWithTag("oneui-shell-desktop").assertIsDisplayed()
        composeRule.onNodeWithTag("oneui-shell-desktop-navigation").assertIsDisplayed()
        composeRule.onNodeWithTag("oneui-shell-desktop-content").assertIsDisplayed()
        composeRule.onNodeWithText("Shell title").assertIsDisplayed()
        composeRule.onNodeWithText("Shell subtitle").assertIsDisplayed()
        composeRule.onNodeWithTag("oneui-shell-desktop-context-bar").assertIsDisplayed()
        composeRule.onNodeWithTag("context-bar-fixture").assertIsDisplayed()
        composeRule.onNodeWithTag("action-slot").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Header action").assertIsDisplayed()
        assertTagAbsent("oneui-shell-compact-drawer")
        assertContentDescriptionAbsent("Open navigation")
        listOf("Flux", "File Browser", "FileBrowser", "file-browser").forEach(::assertTextAbsent)
    }

    @Test
    fun compactOverrideRendersOverlayNavigationAffordance() {
        setShellContent(layoutMode = OneUiLayoutMode.Compact)

        composeRule.onNodeWithTag("oneui-shell-compact").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Open navigation").assertIsDisplayed()
        assertTagAbsent("oneui-shell-desktop-navigation")
        assertTagAbsent("oneui-shell-desktop-context-bar")
        listOf("Flux", "File Browser", "FileBrowser", "file-browser").forEach(::assertTextAbsent)

        composeRule.onNodeWithContentDescription("Open navigation").performClick()
        composeRule.onNodeWithTag("oneui-shell-compact-drawer").assertIsDisplayed()
    }

    @Test
    fun desktopDestinationSelectionUsesTheExistingCallback() {
        val selections = mutableListOf<String>()
        setShellContent(
            layoutMode = OneUiLayoutMode.Desktop,
            onDestinationSelected = { selections += it },
        )

        composeRule.onNodeWithContentDescription("Second destination").performClick()

        assertTrue(selections == listOf("second"))
    }

    @Test
    fun rtlDesktopPaneRemainsOnTheLeadingEdge() {
        setShellContent(
            layoutMode = OneUiLayoutMode.Desktop,
            layoutDirection = LayoutDirection.Rtl,
        )

        val directChildTags = composeRule
            .onNodeWithTag("oneui-shell-desktop", useUnmergedTree = true)
            .fetchSemanticsNode()
            .children
            .mapNotNull { child ->
                runCatching {
                    child.config[SemanticsProperties.TestTag] as String
                }.getOrNull()
            }

        val navigationIndex = directChildTags.indexOf("oneui-shell-desktop-navigation")
        val contentIndex = directChildTags.indexOf("oneui-shell-desktop-content")
        assertTrue(navigationIndex >= 0)
        assertTrue(contentIndex >= 0)
        assertTrue(navigationIndex < contentIndex)
    }

    @Test
    fun reducedMotionDesktopStructureDoesNotAnimateThePermanentPane() {
        composeRule.mainClock.autoAdvance = false
        setShellContent(
            layoutMode = OneUiLayoutMode.Desktop,
            reducedMotion = true,
        )

        composeRule.onNodeWithTag("oneui-shell-desktop-navigation").assertIsDisplayed()
        composeRule.onNodeWithTag("oneui-shell-desktop-content").assertIsDisplayed()
        assertTagAbsent("oneui-shell-compact-drawer")
    }

    private fun assertTagAbsent(tag: String) {
        composeRule.onAllNodesWithTag(tag).assertCountEquals(0)
    }

    private fun assertContentDescriptionAbsent(description: String) {
        composeRule.onAllNodesWithContentDescription(description).assertCountEquals(0)
    }

    private fun assertTextAbsent(text: String) {
        composeRule.onAllNodesWithText(text, useUnmergedTree = true).assertCountEquals(0)
    }

    private fun setShellContent(
        layoutMode: OneUiLayoutMode,
        layoutDirection: LayoutDirection = LayoutDirection.Ltr,
        reducedMotion: Boolean = false,
        onDestinationSelected: (String) -> Unit = {},
    ) {
        composeRule.setContent {
            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                OneUiTheme(reducedMotion = reducedMotion) {
                    OneUiAppShell(
                        destinations = listOf(
                            OneUiAppShellDestination(
                                id = "first",
                                label = "First",
                                icon = OneUiIcons.Home,
                            ),
                            OneUiAppShellDestination(
                                id = "second",
                                label = "Second",
                                icon = OneUiIcons.Grid,
                            ),
                        ),
                        selectedId = "first",
                        onDestinationSelected = onDestinationSelected,
                        title = "Shell title",
                        subtitle = "Shell subtitle",
                        layoutMode = layoutMode,
                        headerAction = {
                            OneUiIconButton(
                                icon = OneUiIcons.Settings,
                                contentDescription = "Header action",
                                onClick = {},
                            )
                        },
                        desktopContextBar = {
                            Box(Modifier.testTag("context-bar-fixture")) {
                                Text("Context bar")
                            }
                        },
                        actions = {
                            Text("Action slot", Modifier.testTag("action-slot"))
                        },
                    ) {
                        Text("First content", Modifier.testTag("content-body"))
                    }
                }
            }
        }
    }
}
