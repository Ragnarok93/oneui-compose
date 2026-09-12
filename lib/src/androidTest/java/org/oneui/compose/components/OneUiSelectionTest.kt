package org.oneui.compose.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.pressKey
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.oneui.compose.components.selection.OneUiCheckbox
import org.oneui.compose.components.selection.OneUiRadioButton
import org.oneui.compose.components.selection.OneUiSwitch
import org.oneui.compose.components.selection.OneUiTriStateCheckbox
import org.oneui.compose.theme.OneUiTheme

class OneUiSelectionTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun selectionControlsExposeNativeRolesAndStates() {
        composeRule.setContent {
            OneUiTheme {
                OneUiSwitch(
                    checked = true,
                    onCheckedChange = {},
                    modifier = Modifier.testTag("switch"),
                )
                OneUiCheckbox(
                    checked = true,
                    onCheckedChange = {},
                    modifier = Modifier.testTag("checkbox"),
                )
                OneUiTriStateCheckbox(
                    state = ToggleableState.Indeterminate,
                    onClick = {},
                    modifier = Modifier.testTag("tri-state"),
                )
                OneUiRadioButton(
                    selected = true,
                    onClick = {},
                    modifier = Modifier.testTag("radio"),
                )
            }
        }

        composeRule.onNodeWithTag("switch")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Switch))
            .assertIsOn()
        composeRule.onNodeWithTag("checkbox")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Checkbox))
            .assertIsOn()
        composeRule.onNodeWithTag("tri-state")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Checkbox))
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ToggleableState,
                    ToggleableState.Indeterminate,
                ),
            )
        composeRule.onNodeWithTag("radio")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.RadioButton))
            .assertIsSelected()
    }

    @Test
    fun disabledControlsSuppressCallbacks() {
        var checkboxChanges = 0
        var switchChanges = 0
        var radioClicks = 0
        composeRule.setContent {
            OneUiTheme {
                OneUiCheckbox(
                    checked = false,
                    onCheckedChange = { checkboxChanges++ },
                    enabled = false,
                    modifier = Modifier.testTag("disabled-checkbox"),
                )
                OneUiSwitch(
                    checked = false,
                    onCheckedChange = { switchChanges++ },
                    enabled = false,
                    modifier = Modifier.testTag("disabled-switch"),
                )
                OneUiRadioButton(
                    selected = false,
                    onClick = { radioClicks++ },
                    enabled = false,
                    modifier = Modifier.testTag("disabled-radio"),
                )
            }
        }

        composeRule.onNodeWithTag("disabled-checkbox").assertIsNotEnabled().performClick()
        composeRule.onNodeWithTag("disabled-switch").assertIsNotEnabled().performClick()
        composeRule.onNodeWithTag("disabled-radio").assertIsNotEnabled().performClick()

        assertEquals(0, checkboxChanges)
        assertEquals(0, switchChanges)
        assertEquals(0, radioClicks)
    }

    @Test
    fun keyboardAndDpadCenterActivateControls() {
        val switchState = mutableStateOf(false)
        val radioState = mutableStateOf(false)
        composeRule.setContent {
            OneUiTheme {
                OneUiSwitch(
                    checked = switchState.value,
                    onCheckedChange = { switchState.value = it },
                    modifier = Modifier.testTag("keyboard-switch"),
                )
                OneUiRadioButton(
                    selected = radioState.value,
                    onClick = { radioState.value = true },
                    modifier = Modifier.testTag("dpad-radio"),
                )
            }
        }

        composeRule.onNodeWithTag("keyboard-switch")
            .performKeyInput { pressKey(Key.Spacebar) }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("keyboard-switch").assertIsOn()

        composeRule.onNodeWithTag("dpad-radio")
            .performKeyInput { pressKey(Key.DirectionCenter) }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("dpad-radio").assertIsSelected()
    }
}
