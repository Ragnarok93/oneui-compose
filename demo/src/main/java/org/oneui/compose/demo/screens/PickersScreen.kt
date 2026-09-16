package org.oneui.compose.demo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalTime
import org.oneui.compose.components.buttons.OneUiTextButton
import org.oneui.compose.picker.NumberPicker
import org.oneui.compose.picker.color.SimpleColorPickerDefaults
import org.oneui.compose.picker.color.SimpleColorPickerPopup
import org.oneui.compose.picker.time.DatePicker
import org.oneui.compose.picker.time.DatePickerDialog
import org.oneui.compose.picker.time.TimePicker
import org.oneui.compose.picker.time.TimePickerDialog
import org.oneui.compose.picker.time.TimePickerState
import org.oneui.compose.picker.time.rememberDatePickerState
import org.oneui.compose.theme.OneUiTheme

/**
 * Compose-native parity catalog for the OneUI8 sample picker destination.
 *
 * The reference exposes seven picker demonstrations. This screen keeps each example interactive
 * and uses the reusable picker implementations from :lib rather than AndroidView wrappers.
 */
@Composable
fun PickersScreen(modifier: Modifier = Modifier) {
    var firstNumber by rememberSaveable { mutableIntStateOf(1) }
    var secondNumber by rememberSaveable { mutableIntStateOf(15) }
    var thirdNumber by rememberSaveable { mutableIntStateOf(30) }

    val inlineTime = remember { TimePickerState(LocalTime.of(10, 30)) }
    var selectedDialogTime by remember { mutableStateOf(LocalTime.of(8, 15)) }
    var showTimeDialog by rememberSaveable { mutableStateOf(false) }

    val inlineDate = rememberDatePickerState(LocalDate.now())
    var selectedDialogDate by remember { mutableStateOf(LocalDate.now()) }
    var showDateDialog by rememberSaveable { mutableStateOf(false) }

    var spinMonth by rememberSaveable { mutableIntStateOf(LocalDate.now().monthValue) }
    var spinDay by rememberSaveable { mutableIntStateOf(LocalDate.now().dayOfMonth) }
    var spinYear by rememberSaveable { mutableIntStateOf(LocalDate.now().year) }

    val bedtime = remember { TimePickerState(LocalTime.of(22, 30)) }
    val wakeTime = remember { TimePickerState(LocalTime.of(7, 0)) }
    val startTime = remember { TimePickerState(LocalTime.of(9, 0)) }
    val endTime = remember { TimePickerState(LocalTime.of(17, 30)) }

    var selectedColor by remember { mutableStateOf(SimpleColorPickerDefaults.colors.first()) }
    var showColorPicker by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("catalog-pickers"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 12.dp,
            bottom = 40.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            PickerSection(
                title = "Number picker",
                subtitle = "Three independent spinning number columns",
                testTag = "picker-number-triple",
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    NumberPicker(
                        modifier = Modifier.weight(1f).height(132.dp),
                        values = (0..9).toList(),
                        startValue = firstNumber,
                        onValueChange = { firstNumber = it },
                    )
                    NumberPicker(
                        modifier = Modifier.weight(1f).height(132.dp),
                        values = (0..59).toList(),
                        startValue = secondNumber,
                        onValueChange = { secondNumber = it },
                        fillUpWithZeros = true,
                    )
                    NumberPicker(
                        modifier = Modifier.weight(1f).height(132.dp),
                        values = (0..59).toList(),
                        startValue = thirdNumber,
                        onValueChange = { thirdNumber = it },
                        fillUpWithZeros = true,
                    )
                }
                PickerValue("Selected", "%d : %02d : %02d".format(firstNumber, secondNumber, thirdNumber))
            }
        }

        item {
            PickerSection(
                title = "Time picker",
                subtitle = "Inline picker and dialog presentation",
                testTag = "picker-time-inline-dialog",
            ) {
                TimePicker(
                    state = inlineTime,
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                )
                PickerValue("Inline", inlineTime.time.toString())
                OneUiTextButton(onClick = { showTimeDialog = true }) {
                    Text("Open time picker dialog")
                }
                PickerValue("Dialog result", selectedDialogTime.toString())
            }
        }

        item {
            PickerSection(
                title = "Date picker",
                subtitle = "Calendar picker and dialog presentation",
                testTag = "picker-date-inline-dialog",
            ) {
                DatePicker(
                    state = inlineDate,
                    modifier = Modifier.fillMaxWidth(),
                )
                PickerValue("Inline", inlineDate.date.toString())
                OneUiTextButton(onClick = { showDateDialog = true }) {
                    Text("Open date picker dialog")
                }
                PickerValue("Dialog result", selectedDialogDate.toString())
            }
        }

        item {
            PickerSection(
                title = "Spinning date picker",
                subtitle = "Month, day and year wheel layout",
                testTag = "picker-spinning-date",
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    NumberPicker(
                        modifier = Modifier.weight(1f).height(132.dp),
                        values = (1..12).toList(),
                        startValue = spinMonth,
                        onValueChange = { spinMonth = it },
                    )
                    NumberPicker(
                        modifier = Modifier.weight(1f).height(132.dp),
                        values = (1..31).toList(),
                        startValue = spinDay,
                        onValueChange = { spinDay = it },
                    )
                    NumberPicker(
                        modifier = Modifier.weight(1.35f).height(132.dp),
                        values = (2020..2035).toList(),
                        startValue = spinYear.coerceIn(2020, 2035),
                        onValueChange = { spinYear = it },
                        infiniteScroll = false,
                    )
                }
                PickerValue("Selected", "%02d/%02d/%04d".format(spinMonth, spinDay, spinYear))
            }
        }

        item {
            PickerSection(
                title = "Sleep time picker",
                subtitle = "Bedtime and wake-up time pair",
                testTag = "picker-sleep-time",
            ) {
                PickerSubheading("Bedtime")
                TimePicker(
                    state = bedtime,
                    modifier = Modifier.fillMaxWidth().height(142.dp),
                )
                PickerSubheading("Wake up")
                TimePicker(
                    state = wakeTime,
                    modifier = Modifier.fillMaxWidth().height(142.dp),
                )
                PickerValue("Sleep window", "${bedtime.time} → ${wakeTime.time}")
            }
        }

        item {
            PickerSection(
                title = "Start / end time picker",
                subtitle = "Independent start and end time selection",
                testTag = "picker-start-end-time",
            ) {
                PickerSubheading("Start")
                TimePicker(
                    state = startTime,
                    modifier = Modifier.fillMaxWidth().height(142.dp),
                )
                PickerSubheading("End")
                TimePicker(
                    state = endTime,
                    modifier = Modifier.fillMaxWidth().height(142.dp),
                )
                PickerValue("Range", "${startTime.time} → ${endTime.time}")
            }
        }

        item {
            PickerSection(
                title = "Color picker",
                subtitle = "One UI quick color palette",
                testTag = "picker-color",
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(selectedColor),
                    )
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "Selected color",
                            color = OneUiTheme.colors.primaryText,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "ARGB %08X".format(selectedColor.value.toLong().toInt()),
                            color = OneUiTheme.colors.secondaryText,
                            fontSize = 13.sp,
                        )
                    }
                    OneUiTextButton(onClick = { showColorPicker = true }) {
                        Text("Choose")
                    }
                }
                if (showColorPicker) {
                    SimpleColorPickerPopup(
                        selectedColor = selectedColor,
                        onColorSelected = {
                            selectedColor = it
                            showColorPicker = false
                        },
                        onDismissRequest = { showColorPicker = false },
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
}

@Composable
private fun PickerSection(
    title: String,
    subtitle: String,
    testTag: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clip(RoundedCornerShape(28.dp))
            .background(OneUiTheme.colors.surfaceElevated)
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = title,
            color = OneUiTheme.colors.primaryText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = subtitle,
            color = OneUiTheme.colors.secondaryText,
            fontSize = 13.sp,
        )
        Spacer(Modifier.height(2.dp))
        content()
    }
}

@Composable
private fun PickerSubheading(text: String) {
    Text(
        text = text,
        color = OneUiTheme.colors.primaryText,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun PickerValue(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = OneUiTheme.colors.secondaryText, fontSize = 13.sp)
        Spacer(Modifier.width(12.dp))
        Text(value, color = OneUiTheme.colors.primaryText, fontSize = 13.sp)
    }
}
