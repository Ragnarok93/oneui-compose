package org.oneui.compose.components

import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.oneui.compose.components.buttons.OneUiButton
import org.oneui.compose.theme.OneUiTheme

class OneUiButtonsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun disabledButtonDoesNotClick() {
        var clicks = 0
        composeRule.setContent {
            OneUiTheme {
                OneUiButton(
                    onClick = { clicks++ },
                    enabled = false,
                ) {
                    Text("Save")
                }
            }
        }

        composeRule.onNodeWithText("Save").performClick()
        assertEquals(0, clicks)
    }

    @Test
    fun loadingKeepsButtonBoundsStable() {
        val loading = mutableStateOf(false)
        composeRule.setContent {
            OneUiTheme {
                OneUiButton(
                    onClick = {},
                    loading = loading.value,
                    modifier = Modifier.testTag("loading-button"),
                ) {
                    Text("Save changes")
                }
            }
        }

        val before = composeRule.onNodeWithTag("loading-button").getUnclippedBoundsInRoot()
        composeRule.runOnUiThread { loading.value = true }
        composeRule.waitForIdle()
        val after = composeRule.onNodeWithTag("loading-button").getUnclippedBoundsInRoot()

        assertEquals(before.right - before.left, after.right - after.left)
        assertEquals(before.bottom - before.top, after.bottom - after.top)
    }
}
