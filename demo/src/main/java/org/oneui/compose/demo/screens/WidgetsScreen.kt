package org.oneui.compose.demo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.buttons.OneUiButton
import org.oneui.compose.components.buttons.OneUiButtonDefaults
import org.oneui.compose.components.buttons.OneUiFilledButton
import org.oneui.compose.components.buttons.OneUiFloatingActionBar
import org.oneui.compose.components.buttons.OneUiFloatingActionItem
import org.oneui.compose.components.buttons.OneUiOutlinedButton
import org.oneui.compose.components.buttons.OneUiTextButton
import org.oneui.compose.components.selection.OneUiCheckbox
import org.oneui.compose.components.selection.OneUiRadioButton
import org.oneui.compose.components.selection.OneUiSwitch
import org.oneui.compose.demo.CatalogSection
import org.oneui.compose.icons.OneUiIcons

@Composable
fun WidgetsScreen(modifier: Modifier = Modifier) {
    var switchChecked by remember { mutableStateOf(true) }
    var checkboxChecked by remember { mutableStateOf(true) }
    var radioSelected by remember { mutableIntStateOf(0) }
    var actionIndex by remember { mutableIntStateOf(1) }

    LazyColumn(
        modifier = modifier.testTag("catalog-misc-widgets"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            CatalogSection(
                title = "Compound buttons",
                subtitle = "Stable Compose-native SESL8 switch, checkbox and radio controls.",
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    OneUiSwitch(
                        checked = switchChecked,
                        onCheckedChange = { switchChecked = it },
                    )
                    OneUiCheckbox(
                        checked = checkboxChecked,
                        onCheckedChange = { checkboxChecked = it },
                    )
                    OneUiRadioButton(
                        selected = radioSelected == 0,
                        onClick = { radioSelected = 0 },
                    )
                    OneUiRadioButton(
                        selected = radioSelected == 1,
                        onClick = { radioSelected = 1 },
                    )
                }
            }
        }
        item {
            CatalogSection(
                title = "Buttons",
                subtitle = "Reference button families are being consolidated onto the shared OneUiButton primitive.",
            ) {
                OneUiButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = OneUiButtonDefaults.neutralColors(),
                ) { Text("Default") }
                OneUiFilledButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Contained primary") }
                OneUiOutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Outline") }
                OneUiTextButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Transparent") }
            }
        }
        item {
            CatalogSection(
                title = "Floating action bar",
                subtitle = "Two-option One UI selected-surface motion.",
            ) {
                OneUiFloatingActionBar(
                    selectedIndex = actionIndex,
                    items = listOf(
                        OneUiFloatingActionItem("Action #1", OneUiIcons.ArrowUp),
                        OneUiFloatingActionItem("Action #2", OneUiIcons.ArrowDown),
                    ),
                    onSelected = { actionIndex = it },
                )
            }
        }
    }
}
