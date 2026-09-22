package org.oneui.compose.picker.time

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import org.oneui.compose.R
import org.oneui.compose.components.navigation.OneUiNavigationItem
import org.oneui.compose.components.navigation.OneUiTabStyle
import org.oneui.compose.components.navigation.OneUiTabs
import org.oneui.compose.dialog.AlertDialog

/**
 * Compose-native counterpart to SESL's start/end time dialog.
 *
 * A single time wheel is shared by Start and End tabs, matching the reference dialog's interaction
 * model while keeping both selections independently owned by the caller.
 */
@Composable
fun StartEndTimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeSelected: (start: LocalTime, end: LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    initialStartTime: LocalTime = LocalTime.MIDNIGHT,
    initialEndTime: LocalTime = LocalTime.of(10, 0),
    config: TimePickerConfig = timePickerConfig(),
    title: String = "Start / end time",
    startLabel: String = "Start",
    endLabel: String = "End",
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val startState = remember(initialStartTime) { TimePickerState(initialStartTime) }
    val endState = remember(initialEndTime) { TimePickerState(initialEndTime) }
    val labels = remember(startLabel, endLabel) {
        listOf(
            OneUiNavigationItem(id = "start", label = startLabel),
            OneUiNavigationItem(id = "end", label = endLabel),
        )
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        positiveButtonLabel = stringResource(R.string.sesl_picker_done),
        onPositiveButtonClick = {
            onTimeSelected(startState.time, endState.time)
            onDismissRequest()
        },
        negativeButtonLabel = stringResource(R.string.sesl_picker_cancel),
        onNegativeButtonClick = onDismissRequest,
        body = {
            Column(Modifier.fillMaxWidth()) {
                OneUiTabs(
                    items = labels,
                    selectedIndex = selectedTab,
                    onSelected = { selectedTab = it },
                    style = OneUiTabStyle.Rounded,
                )
                Spacer(Modifier.height(16.dp))
                TimePicker(
                    state = if (selectedTab == 0) startState else endState,
                    config = config,
                )
            }
        },
    )
}
