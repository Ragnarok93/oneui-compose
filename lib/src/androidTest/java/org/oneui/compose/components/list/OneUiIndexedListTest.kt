package org.oneui.compose.components.list

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test
import org.oneui.compose.theme.OneUiTheme

class OneUiIndexedListTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun indexedListExposesReusableFastScrollerSections() {
        composeRule.setContent {
            OneUiTheme {
                OneUiIndexedList(
                    items = listOf("Alpha", "Beta", "Charlie"),
                    key = { it },
                    label = { it },
                    modifier = Modifier
                        .height(220.dp)
                        .testTag("indexed-list"),
                ) { item ->
                    Text(item)
                }
            }
        }

        composeRule.onNodeWithTag("indexed-list").assertIsDisplayed()
        composeRule.onNodeWithText("Alpha").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Scroll to B").assertIsDisplayed()
    }
}
