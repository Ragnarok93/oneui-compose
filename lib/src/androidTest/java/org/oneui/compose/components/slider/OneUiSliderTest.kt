package org.oneui.compose.components.slider

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performSemanticsAction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.oneui.compose.theme.OneUiTheme

class OneUiSliderTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun exposesRangeInfoAndSnapsSetProgress() {
        var changed: Float? = null
        composeRule.setContent {
            OneUiTheme {
                OneUiSlider(
                    value = 15f,
                    onValueChange = { changed = it },
                    modifier = Modifier.testTag("slider"),
                    valueRange = 10f..20f,
                    steps = 1,
                )
            }
        }

        val slider = composeRule.onNodeWithTag("slider")
        slider.assert(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ProgressBarRangeInfo,
                ProgressBarRangeInfo(15f, 10f..20f, 1),
            ),
        )
        slider.performSemanticsAction(SemanticsActions.SetProgress) { action ->
            action(14f)
        }

        composeRule.runOnIdle {
            assertEquals(15f, changed ?: Float.NaN, 0f)
        }
    }

    @Test
    fun disabledSliderRejectsSetProgress() {
        var changed: Float? = null
        composeRule.setContent {
            OneUiTheme {
                OneUiSlider(
                    value = 0.5f,
                    onValueChange = { changed = it },
                    modifier = Modifier.testTag("disabled-slider"),
                    enabled = false,
                )
            }
        }

        val slider = composeRule.onNodeWithTag("disabled-slider")
        slider.assertIsNotEnabled()
        slider.performSemanticsAction(SemanticsActions.SetProgress) { action ->
            action(0.75f)
        }

        composeRule.runOnIdle {
            assertNull(changed)
        }
    }
}
