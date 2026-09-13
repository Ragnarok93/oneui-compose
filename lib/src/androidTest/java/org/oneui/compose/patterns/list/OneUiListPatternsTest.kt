package org.oneui.compose.patterns.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test
import org.oneui.compose.components.list.OneUiFastScrollerDisplayMode
import org.oneui.compose.theme.OneUiTheme

class OneUiListPatternsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun selectableListLongPressesIntoSelectionMode() {
        val state = OneUiSelectableListState<Long>()
        val items = listOf(
            TestProfile(1L, "Ada", "github.com/ada"),
            TestProfile(2L, "Grace", "github.com/grace"),
        )

        composeRule.setContent {
            OneUiTheme(reducedMotion = true) {
                OneUiSelectableList(
                    items = items,
                    state = state,
                    key = TestProfile::id,
                    onItemClick = {},
                ) { item, selected, selectionMode ->
                    OneUiSelectableListItem(
                        title = item.name,
                        subtitle = item.url,
                        selected = selected,
                        selectionMode = selectionMode,
                        leadingContent = {
                            Box(Modifier.size(40.dp))
                        },
                    )
                }
            }
        }

        composeRule.onNodeWithText("Ada").performTouchInput { longClick() }
        composeRule.onNodeWithText("Ada").assertIsSelected()
    }

    @Test
    fun selectableListCanExposeReusableIndexedFastScroll() {
        val state = OneUiSelectableListState<Long>()
        val items = listOf(
            TestProfile(1L, "Ada", "github.com/ada"),
            TestProfile(2L, "Grace", "github.com/grace"),
            TestProfile(3L, "Margaret", "github.com/margaret"),
        )

        composeRule.setContent {
            OneUiTheme(reducedMotion = true) {
                OneUiSelectableList(
                    items = items,
                    state = state,
                    key = TestProfile::id,
                    onItemClick = {},
                    indexLabel = TestProfile::name,
                    fastScrollerDisplayMode = OneUiFastScrollerDisplayMode.Text,
                ) { item, selected, selectionMode ->
                    OneUiSelectableListItem(
                        title = item.name,
                        selected = selected,
                        selectionMode = selectionMode,
                    )
                }
            }
        }

        composeRule.onNodeWithContentDescription("Scroll to A").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Scroll to G").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Scroll to M").assertIsDisplayed()
    }

    @Test
    fun profileShowsIdentityDetailsAndActions() {
        composeRule.setContent {
            OneUiTheme(reducedMotion = true) {
                OneUiProfile(
                    name = "Ada Lovelace",
                    subtitle = "github.com/ada",
                    details = listOf(
                        OneUiProfileDetail("Location", "London"),
                        OneUiProfileDetail("Company", "Analytical Engine"),
                    ),
                    avatar = { Box(Modifier.size(88.dp)) },
                    actions = {
                        OneUiProfileAction(
                            label = "Share",
                            onClick = {},
                            modifier = Modifier.testTag("profile-share"),
                        )
                    },
                )
            }
        }

        composeRule.onNodeWithText("Ada Lovelace").assertIsDisplayed()
        composeRule.onNodeWithText("London").assertIsDisplayed()
        composeRule.onNodeWithTag("profile-share").assertIsDisplayed()
    }

    private data class TestProfile(
        val id: Long,
        val name: String,
        val url: String,
    )
}
