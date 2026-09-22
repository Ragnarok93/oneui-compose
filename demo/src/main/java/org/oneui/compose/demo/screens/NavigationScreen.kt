package org.oneui.compose.demo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.icons.OneUiDrawableCatalog
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.components.navigation.OneUiBottomNavigation
import org.oneui.compose.components.navigation.OneUiBottomTabLayout
import org.oneui.compose.components.navigation.OneUiNavigationItem
import org.oneui.compose.components.navigation.OneUiNavigationRail
import org.oneui.compose.components.navigation.OneUiTabs
import org.oneui.compose.components.navigation.OneUiTabStyle
import org.oneui.compose.theme.OneUiTheme

@Composable
fun NavigationScreen(modifier: Modifier = Modifier) {
    var roundedSelected by rememberSaveable { mutableIntStateOf(0) }
    var subSelected by rememberSaveable { mutableIntStateOf(0) }
    var iconSelected by rememberSaveable { mutableIntStateOf(0) }
    var bottomIconSelected by rememberSaveable { mutableIntStateOf(0) }
    var bottomTextSelected by rememberSaveable { mutableIntStateOf(0) }
    var manySelected by rememberSaveable { mutableIntStateOf(0) }
    var railSelected by rememberSaveable { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("catalog-navigation"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            NavigationSample(
                title = "Rounded text tabs",
                subtitle = "Primary rounded tab treatment",
                testTag = "tabs-rounded-text",
            ) {
                OneUiTabs(
                    items = listOf("Tab 1", "Tab 2").map { OneUiNavigationItem(it, it) },
                    selectedIndex = roundedSelected.coerceAtMost(1),
                    onSelected = { roundedSelected = it },
                    style = OneUiTabStyle.Rounded,
                )
            }
        }

        item {
            NavigationSample(
                title = "Scrollable subtabs",
                subtitle = "Secondary navigation remains reachable in compact widths",
                testTag = "tabs-sub-scrollable",
            ) {
                OneUiTabs(
                    items = (1..8).map { OneUiNavigationItem("subtab-$it", "Subtab $it") },
                    selectedIndex = subSelected.coerceAtMost(7),
                    onSelected = { subSelected = it },
                    scrollable = true,
                    style = OneUiTabStyle.Sub,
                )
            }
        }

        item {
            NavigationSample(
                title = "Main icon tabs",
                subtitle = "Auto-weighted icon destinations",
                testTag = "tabs-main-icon-auto-weight",
            ) {
                OneUiTabs(
                    items = listOf(
                        clockItem("alarm", "Main tab 1", OneUiIcons.clockAlarmTab(false), OneUiIcons.clockAlarmTab(true)),
                        clockItem("timer", "Main tab 2", OneUiIcons.clockTimerTab(false), OneUiIcons.clockTimerTab(true)),
                        clockItem("stopwatch", "Main tab 3", OneUiIcons.clockStopwatchTab(false), OneUiIcons.clockStopwatchTab(true)),
                    ),
                    selectedIndex = iconSelected.coerceAtMost(2),
                    onSelected = { iconSelected = it },
                    showIcons = true,
                    style = OneUiTabStyle.Main,
                )
            }
        }

        item {
            NavigationSample(
                title = "Bottom navigation with overflow",
                subtitle = "Four icon destinations plus More",
                testTag = "bottom-nav-icons-overflow",
            ) {
                OneUiBottomNavigation(
                    items = (1..6).map { index ->
                        OneUiNavigationItem("item-$index", "Item $index", OneUiIcons.Home)
                    },
                    selectedIndex = bottomIconSelected.coerceAtMost(5),
                    onSelected = { bottomIconSelected = it },
                )
            }
        }

        item {
            NavigationSample(
                title = "Text bottom navigation",
                subtitle = "Text-only navigation destinations",
                testTag = "bottom-nav-text",
            ) {
                OneUiBottomNavigation(
                    items = (1..3).map { OneUiNavigationItem("item-$it", "Item $it") },
                    selectedIndex = bottomTextSelected.coerceAtMost(2),
                    onSelected = { bottomTextSelected = it },
                    showIcons = false,
                )
            }
        }

        item {
            NavigationSample(
                title = "13 bottom tabs",
                subtitle = "Large destination set in a horizontally scrollable strip",
                testTag = "bottom-tabs-13-items",
            ) {
                OneUiBottomTabLayout(
                    items = referenceBottomTabItems(),
                    selectedIndex = manySelected,
                    onSelected = { manySelected = it },
                )
            }
        }

        item {
            NavigationSample(
                title = "Navigation rail",
                subtitle = "Collapsed icon destinations used by the adaptive shell",
                testTag = "navigation-rail",
            ) {
                OneUiNavigationRail(
                    items = listOf(
                        OneUiNavigationItem("home", "Home", OneUiIcons.Home),
                        OneUiNavigationItem("search", "Search", OneUiIcons.Search),
                        OneUiNavigationItem("settings", "Settings", OneUiIcons.Settings),
                    ),
                    selectedIndex = railSelected,
                    onSelected = { railSelected = it },
                    modifier = Modifier.testTag("navigation-rail-control"),
                )
            }
        }
    }
}

private fun clockItem(
    id: String,
    label: String,
    icon: OneUiIcon,
    selectedIcon: OneUiIcon,
) = OneUiNavigationItem(id, label, icon, selectedIcon)

private fun referenceBottomTabItems(): List<OneUiNavigationItem> {
    val resourceNames = listOf(
        null,
        null,
        null,
        "ic_oui_location_outline",
        "ic_oui_advanced_call_outline",
        "ic_oui_brightness_outline",
        "ic_oui_settings_outline",
        "ic_oui_gif",
        "ic_oui_import",
        "ic_oui_location_outline",
        "ic_oui_advanced_call_outline",
        "ic_oui_brightness_outline",
        "ic_oui_settings_outline",
    )
    val clockIcons = listOf(
        OneUiIcons.clockAlarmTab(false) to OneUiIcons.clockAlarmTab(true),
        OneUiIcons.clockTimerTab(false) to OneUiIcons.clockTimerTab(true),
        OneUiIcons.clockStopwatchTab(false) to OneUiIcons.clockStopwatchTab(true),
    )
    return (1..13).map { index ->
        val clock = clockIcons.getOrNull(index - 1)
        val icon = clock?.first ?: resourceNames[index - 1]?.let(::referenceIcon)
        val selectedIcon = clock?.second ?: resourceNames[index - 1]?.let(::referenceIcon)
        OneUiNavigationItem("nav-$index", "Nav item $index", icon, selectedIcon)
    }
}

private fun referenceIcon(name: String): OneUiIcon =
    requireNotNull(OneUiDrawableCatalog.icon(name)) { "Missing pinned One UI icon: $name" }

@Composable
private fun NavigationSample(
    title: String,
    subtitle: String,
    testTag: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clip(RoundedCornerShape(28.dp))
            .background(OneUiTheme.colors.surfaceElevated)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
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
