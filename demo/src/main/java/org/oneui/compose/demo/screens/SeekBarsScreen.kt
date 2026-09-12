package org.oneui.compose.demo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.components.slider.OneUiSlider
import org.oneui.compose.components.slider.OneUiSliderMode
import org.oneui.compose.components.slider.OneUiSliderOrientation
import org.oneui.compose.demo.CatalogSection
import org.oneui.compose.theme.OneUiTheme

@Composable
fun SeekBarsScreen(modifier: Modifier = Modifier) {
    var standard by remember { mutableFloatStateOf(0.35f) }
    var expand by remember { mutableFloatStateOf(0.52f) }
    var overlap by remember { mutableFloatStateOf(0.72f) }
    var level by remember { mutableFloatStateOf(15f) }
    var seamlessLevel by remember { mutableFloatStateOf(3.4f) }
    var plusSeamless by remember { mutableFloatStateOf(9.5f) }
    var plusStepped by remember { mutableFloatStateOf(1f) }
    var vertical by remember { mutableFloatStateOf(0.45f) }
    var verticalExpand by remember { mutableFloatStateOf(0.62f) }

    LazyColumn(
        modifier = modifier.testTag("catalog-seek-bars"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            CatalogSection(title = "Horizontal") {
                LabeledSlider("Standard", standard) {
                    OneUiSlider(
                        value = standard,
                        onValueChange = { standard = it },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                LabeledSlider("Expand", expand) {
                    OneUiSlider(
                        value = expand,
                        onValueChange = { expand = it },
                        modifier = Modifier.fillMaxWidth(),
                        mode = OneUiSliderMode.Expand,
                    )
                }
                LabeledSlider("Expand · overlap / dual-color threshold 70", overlap) {
                    OneUiSlider(
                        value = overlap,
                        onValueChange = { overlap = it },
                        modifier = Modifier.fillMaxWidth(),
                        mode = OneUiSliderMode.Expand,
                        warningRange = 0.7f..1f,
                    )
                }
                LabeledSlider("Level bar · 10…20", level) {
                    OneUiSlider(
                        value = level,
                        onValueChange = { level = it },
                        modifier = Modifier.fillMaxWidth(),
                        valueRange = 10f..20f,
                        steps = 9,
                    )
                }
                LabeledSlider("Level bar · seamless 0…7", seamlessLevel) {
                    OneUiSlider(
                        value = seamlessLevel,
                        onValueChange = { seamlessLevel = it },
                        modifier = Modifier.fillMaxWidth(),
                        valueRange = 0f..7f,
                    )
                }
                LabeledSlider("SeekBarPlus · seamless 0…20", plusSeamless) {
                    OneUiSlider(
                        value = plusSeamless,
                        onValueChange = { plusSeamless = it },
                        modifier = Modifier.fillMaxWidth(),
                        valueRange = 0f..20f,
                    )
                }
                LabeledSlider("SeekBarPlus · stepped 0…2", plusStepped) {
                    OneUiSlider(
                        value = plusStepped,
                        onValueChange = { plusStepped = it },
                        modifier = Modifier.fillMaxWidth(),
                        valueRange = 0f..2f,
                        steps = 1,
                    )
                }
            }
        }
        item {
            CatalogSection(title = "Vertical") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OneUiSlider(
                            value = vertical,
                            onValueChange = { vertical = it },
                            modifier = Modifier.height(200.dp),
                            orientation = OneUiSliderOrientation.Vertical,
                        )
                        Caption("Standard")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OneUiSlider(
                            value = verticalExpand,
                            onValueChange = { verticalExpand = it },
                            modifier = Modifier.height(200.dp),
                            orientation = OneUiSliderOrientation.Vertical,
                            mode = OneUiSliderMode.Expand,
                        )
                        Caption("Expand")
                    }
                }
            }
        }
    }
}

@Composable
private fun LabeledSlider(
    label: String,
    value: Float,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(label, color = OneUiTheme.colors.primaryText, fontSize = 14.sp)
            Text(
                text = if (value % 1f == 0f) value.toInt().toString() else "%.2f".format(value),
                color = OneUiTheme.colors.secondaryText,
                fontSize = 12.sp,
            )
        }
        content()
    }
}

@Composable
private fun Caption(text: String) {
    Text(
        text = text,
        color = OneUiTheme.colors.secondaryText,
        fontSize = 12.sp,
        modifier = Modifier.padding(top = 8.dp),
    )
}
