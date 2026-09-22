package org.oneui.compose.demo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalTime
import org.oneui.compose.components.buttons.OneUiContainedButton
import org.oneui.compose.components.input.OneUiSpinner
import org.oneui.compose.picker.NumberPicker
import org.oneui.compose.picker.StringPicker
import org.oneui.compose.picker.color.SimpleColorPickerDefaults
import org.oneui.compose.picker.color.SimpleColorPickerDialog
import org.oneui.compose.picker.time.DatePicker
import org.oneui.compose.picker.time.DatePickerDialog
import org.oneui.compose.picker.time.StartEndTimePickerDialog
import org.oneui.compose.picker.time.TimePicker
import org.oneui.compose.picker.time.TimePickerDialog
import org.oneui.compose.picker.time.TimePickerState
import org.oneui.compose.picker.time.rememberDatePickerState
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.theme.OneUiTheme

private enum class PickerDemo(val label: String, val testTag: String) {
    Number("NumberPicker", "picker-number-triple"),
    Time("TimePicker", "picker-time-inline"),
    Date("DatePicker", "picker-date-inline"),
    SpinningDate("SpinningDatePicker", "picker-spinning-date"),
    SleepTime("SleepTimePicker", "picker-sleep-time"),
}

/** Measurements from the pinned SESL8 `fragment_pickers.xml` dialog section. */
internal object PickerReferenceDefaults {
    val DialogButtonWidth = 250.dp
}

/** Compose-native port of the pinned `fragment_pickers.xml` surface. */
@Composable
fun PickersScreen(modifier: Modifier = Modifier) {
    var selectedDemo by rememberSaveable { mutableIntStateOf(PickerDemo.Number.ordinal) }
    var firstNumber by rememberSaveable { mutableIntStateOf(50) }
    var secondNumber by rememberSaveable { mutableIntStateOf(8) }
    var thirdValue by rememberSaveable { mutableStateOf("A") }

    val inlineTime = remember { TimePickerState(LocalTime.of(10, 30)) }
    val inlineDate = rememberDatePickerState(LocalDate.now())
    var spinMonth by rememberSaveable { mutableIntStateOf(LocalDate.now().monthValue) }
    var spinDay by rememberSaveable { mutableIntStateOf(LocalDate.now().dayOfMonth) }
    var spinYear by rememberSaveable { mutableIntStateOf(LocalDate.now().year) }
    val bedtime = remember { TimePickerState(LocalTime.of(22, 30)) }
    val wakeTime = remember { TimePickerState(LocalTime.of(7, 0)) }

    var selectedDialogTime by remember { mutableStateOf(LocalTime.of(8, 15)) }
    var selectedDialogDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedStartTime by remember { mutableStateOf(LocalTime.MIDNIGHT) }
    var selectedEndTime by remember { mutableStateOf(LocalTime.of(10, 0)) }
    var selectedColor by remember { mutableStateOf(Color.Red) }
    var showTimeDialog by rememberSaveable { mutableStateOf(false) }
    var showDateDialog by rememberSaveable { mutableStateOf(false) }
    var showStartEndDialog by rememberSaveable { mutableStateOf(false) }
    var showColorDialog by rememberSaveable { mutableStateOf(false) }

    val selected = PickerDemo.entries.getOrElse(selectedDemo) { PickerDemo.Number }
    val pickerTextStyle = OneUITheme.types.numberPicker.copy(fontSize = 42.sp)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("catalog-pickers"),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("picker-mode-spinner"),
            ) {
                OneUiSpinner(
                    selectedIndex = selected.ordinal,
                    entries = PickerDemo.entries.map(PickerDemo::label),
                    onSelected = { selectedDemo = it },
                    contentDescription = "Choose picker sample",
                )
            }
        }
        item {
            PickerReferenceSurface(testTag = selected.testTag) {
                when (selected) {
                    PickerDemo.Number -> NumberPickerSample(
                        firstNumber = firstNumber,
                        secondNumber = secondNumber,
                        thirdValue = thirdValue,
                        onFirstNumberChanged = { firstNumber = it },
                        onSecondNumberChanged = { secondNumber = it },
                        onThirdValueChanged = { thirdValue = it },
                    )

                    PickerDemo.Time -> TimePicker(
                        state = inlineTime,
                        textStyle = pickerTextStyle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp),
                    )

                    PickerDemo.Date -> DatePicker(
                        state = inlineDate,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    PickerDemo.SpinningDate -> SpinningDatePickerSample(
                        month = spinMonth,
                        day = spinDay,
                        year = spinYear,
                        onMonthChanged = { spinMonth = it },
                        onDayChanged = { spinDay = it },
                        onYearChanged = { spinYear = it },
                    )

                    PickerDemo.SleepTime -> SleepTimePickerSample(
                        bedtime = bedtime,
                        wakeTime = wakeTime,
                        textStyle = pickerTextStyle,
                    )
                }
            }
        }
        item { ReferenceSeparator("Dialogs") }
        item {
            PickerReferenceSurface(testTag = "picker-dialogs") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    PickerDialogButton(
                        label = "Date",
                        testTag = "picker-date-dialog",
                        onClick = { showDateDialog = true },
                    )
                    PickerDialogButton(
                        label = "Time",
                        testTag = "picker-time-dialog",
                        onClick = { showTimeDialog = true },
                    )
                    PickerDialogButton(
                        label = "Start End Time",
                        testTag = "picker-start-end-time-dialog",
                        onClick = { showStartEndDialog = true },
                    )
                    PickerDialogButton(
                        label = "Color",
                        testTag = "picker-color-dialog",
                        onClick = { showColorDialog = true },
                    )
                }
            }
        }
    }

    if (showTimeDialog) {
        TimePickerDialog(
            initialTime = selectedDialogTime,
            onDismissRequest = { showTimeDialog = false },
            onTimeSelected = {
                selectedDialogTime = it
                showTimeDialog = false
            },
        )
    }
    if (showDateDialog) {
        DatePickerDialog(
            initialSelectedDate = selectedDialogDate,
            onDismissRequest = { showDateDialog = false },
            onDateSelected = {
                selectedDialogDate = it
                showDateDialog = false
            },
        )
    }
    if (showStartEndDialog) {
        StartEndTimePickerDialog(
            initialStartTime = selectedStartTime,
            initialEndTime = selectedEndTime,
            onDismissRequest = { showStartEndDialog = false },
            onTimeSelected = { start, end ->
                selectedStartTime = start
                selectedEndTime = end
                showStartEndDialog = false
            },
        )
    }
    if (showColorDialog) {
        SimpleColorPickerDialog(
            selectionColors = SimpleColorPickerDefaults.colors,
            selectedColor = selectedColor,
            onColorSelected = {
                selectedColor = it
                showColorDialog = false
            },
            onDismissRequest = { showColorDialog = false },
        )
    }
}

@Composable
private fun PickerReferenceSurface(
    testTag: String,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clip(RoundedCornerShape(26.dp))
            .background(OneUiTheme.colors.surfaceElevated)
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
private fun NumberPickerSample(
    firstNumber: Int,
    secondNumber: Int,
    thirdValue: String,
    onFirstNumberChanged: (Int) -> Unit,
    onSecondNumberChanged: (Int) -> Unit,
    onThirdValueChanged: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberPicker(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            values = (1..100).toList(),
            startValue = firstNumber,
            onValueChange = onFirstNumberChanged,
        )
        NumberPicker(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            values = (0..10).toList(),
            startValue = secondNumber,
            onValueChange = onSecondNumberChanged,
            textStyle = OneUITheme.types.numberPicker.copy(fontSize = 50.sp),
        )
        StringPicker(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            values = listOf("A", "B", "C"),
            startValue = thirdValue,
            onValueChange = onThirdValueChanged,
            textStyle = OneUITheme.types.numberPicker.copy(fontSize = 40.sp),
        )
    }
}

@Composable
private fun SpinningDatePickerSample(
    month: Int,
    day: Int,
    year: Int,
    onMonthChanged: (Int) -> Unit,
    onDayChanged: (Int) -> Unit,
    onYearChanged: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberPicker(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            values = (1..12).toList(),
            startValue = month,
            onValueChange = onMonthChanged,
        )
        NumberPicker(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            values = (1..31).toList(),
            startValue = day,
            onValueChange = onDayChanged,
        )
        NumberPicker(
            modifier = Modifier
                .weight(1.25f)
                .fillMaxHeight(),
            values = (2020..2035).toList(),
            startValue = year.coerceIn(2020, 2035),
            onValueChange = onYearChanged,
            infiniteScroll = false,
        )
    }
}

@Composable
private fun SleepTimePickerSample(
    bedtime: TimePickerState,
    wakeTime: TimePickerState,
    textStyle: TextStyle,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        PickerLabel("Bedtime")
        TimePicker(
            state = bedtime,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp),
        )
        PickerLabel("Wake-up time")
        TimePicker(
            state = wakeTime,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp),
        )
    }
}

@Composable
private fun ReferenceSeparator(label: String) {
    Text(
        text = label,
        color = OneUiTheme.colors.secondaryText,
        style = OneUiTheme.typography.sectionLabel,
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
    )
}

@Composable
private fun PickerLabel(label: String) {
    Text(
        text = label,
        color = OneUiTheme.colors.primaryText,
        style = OneUiTheme.typography.listTitle,
        modifier = Modifier.padding(horizontal = 24.dp),
    )
}

@Composable
private fun PickerDialogButton(
    label: String,
    testTag: String,
    onClick: () -> Unit,
) {
    OneUiContainedButton(
        onClick = onClick,
        modifier = Modifier
            .width(PickerReferenceDefaults.DialogButtonWidth)
            .testTag(testTag),
    ) {
        Text(label)
    }
}
