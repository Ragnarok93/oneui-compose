package org.oneui.compose.demo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.components.selection.OneUiRadioButton
import org.oneui.compose.components.selection.OneUiSwitch
import org.oneui.compose.dialog.AlertDialog as OneUiAlertDialog
import org.oneui.compose.theme.OneUiTheme

object StargazersOptionsTestTags {
    const val TriggerDescription = "Stargazers options"
    const val Dialog = "stargazers-options-dialog"
    const val ShowIndexLetters = "stargazers-option-show-index-letters"
    const val AutoHideIndexScroll = "stargazers-option-auto-hide-index-scroll"
    const val ShowCancelButton = "stargazers-option-show-cancel-button"
    const val ActionModeDismiss = "stargazers-option-action-mode-dismiss"
    const val ActionModeNoDismiss = "stargazers-option-action-mode-no-dismiss"
    const val ActionModeConcurrent = "stargazers-option-action-mode-concurrent"
}

@Composable
internal fun StargazersOptionsDialog(
    state: CatalogStargazersOptionsState,
    onStateChange: (CatalogStargazersOptionsState) -> Unit,
) {
    if (!state.isOpen) return

    OneUiAlertDialog(
        modifier = Modifier.testTag(StargazersOptionsTestTags.Dialog),
        onDismissRequest = { onStateChange(state.cancel()) },
        title = {
            Text(CatalogStargazersSettings.DialogTitle)
        },
        body = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
            ) {
                StargazersSwitchOption(
                    label = CatalogStargazersSettings.ShowIndexLettersLabel,
                    checked = state.draft.showIndexLetters,
                    testTag = StargazersOptionsTestTags.ShowIndexLetters,
                    onCheckedChange = { checked ->
                        onStateChange(
                            state.updateDraft(state.draft.copy(showIndexLetters = checked)),
                        )
                    },
                )
                StargazersSwitchOption(
                    label = CatalogStargazersSettings.AutoHideIndexScrollLabel,
                    checked = state.draft.autoHideIndexScroll,
                    testTag = StargazersOptionsTestTags.AutoHideIndexScroll,
                    onCheckedChange = { checked ->
                        onStateChange(
                            state.updateDraft(state.draft.copy(autoHideIndexScroll = checked)),
                        )
                    },
                )
                StargazersSwitchOption(
                    label = CatalogStargazersSettings.ShowCancelButtonLabel,
                    checked = state.draft.showCancelButton,
                    testTag = StargazersOptionsTestTags.ShowCancelButton,
                    onCheckedChange = { checked ->
                        onStateChange(
                            state.updateDraft(state.draft.copy(showCancelButton = checked)),
                        )
                    },
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = CatalogStargazersSettings.ActionModeSearchLabel,
                        color = OneUiTheme.colors.secondaryText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    )
                    Spacer(Modifier.weight(1f))
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .weight(1f),
                        color = OneUiTheme.colors.divider,
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup(),
                ) {
                    StargazersRadioOption(
                        label = "DISMISS",
                        selected = state.draft.actionModeSearch == CatalogActionModeSearch.DISMISS,
                        testTag = StargazersOptionsTestTags.ActionModeDismiss,
                        onClick = {
                            onStateChange(
                                state.updateDraft(
                                    state.draft.copy(actionModeSearch = CatalogActionModeSearch.DISMISS),
                                ),
                            )
                        },
                    )
                    StargazersRadioOption(
                        label = "NO_DISMISS",
                        selected = state.draft.actionModeSearch == CatalogActionModeSearch.NO_DISMISS,
                        testTag = StargazersOptionsTestTags.ActionModeNoDismiss,
                        onClick = {
                            onStateChange(
                                state.updateDraft(
                                    state.draft.copy(actionModeSearch = CatalogActionModeSearch.NO_DISMISS),
                                ),
                            )
                        },
                    )
                    StargazersRadioOption(
                        label = "CONCURRENT",
                        selected = state.draft.actionModeSearch == CatalogActionModeSearch.CONCURRENT,
                        testTag = StargazersOptionsTestTags.ActionModeConcurrent,
                        onClick = {
                            onStateChange(
                                state.updateDraft(
                                    state.draft.copy(actionModeSearch = CatalogActionModeSearch.CONCURRENT),
                                ),
                            )
                        },
                    )
                }
            }
        },
        negativeButtonLabel = "Cancel",
        onNegativeButtonClick = { onStateChange(state.cancel()) },
        positiveButtonLabel = "Apply",
        onPositiveButtonClick = { onStateChange(state.apply()) },
    )
}

@Composable
private fun StargazersSwitchOption(
    label: String,
    checked: Boolean,
    testTag: String,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .toggleable(
                value = checked,
                role = Role.Switch,
                onValueChange = onCheckedChange,
            )
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = OneUiTheme.colors.primaryText,
            fontSize = 15.sp,
        )
        OneUiSwitch(
            checked = checked,
            onCheckedChange = null,
        )
    }
}

@Composable
private fun StargazersRadioOption(
    label: String,
    selected: Boolean,
    testTag: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OneUiRadioButton(
            selected = selected,
            onClick = null,
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp),
            color = OneUiTheme.colors.primaryText,
            fontSize = 14.sp,
        )
    }
}
