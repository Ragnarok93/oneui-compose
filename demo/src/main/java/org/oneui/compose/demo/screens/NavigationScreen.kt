package org.oneui.compose.demo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.navigation.CustomTabItem
import org.oneui.compose.navigation.SubTabItem
import org.oneui.compose.navigation.TabItem
import org.oneui.compose.navigation.Tabs
import org.oneui.compose.theme.OneUiTheme

@Composable
fun NavigationScreen(modifier: Modifier = Modifier) {
    var roundedSelected by rememberSaveable { mutableIntStateOf(0) }
    var subSelected by rememberSaveable { mutableIntStateOf(0) }
    var iconSelected by rememberSaveable { mutableIntStateOf(0) }
    var bottomIconSelected by rememberSaveable { mutableIntStateOf(0) }
    var bottomTextSelected by rememberSaveable { mutableIntStateOf(0) }
    var manySelected by rememberSaveable { mutableIntStateOf(0) }

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
                Tabs(Modifier.fillMaxWidth()) {
                    listOf("Overview", "Details", "Activity").forEachIndexed { index, label ->
                        SubTabItem(
                            modifier = Modifier.weight(1f),
                            onClick = { roundedSelected = index },
                            text = label,
                            selected = roundedSelected == index,
                        )
                    }
                }
            }
        }

        item {
            NavigationSample(
                title = "Scrollable subtabs",
                subtitle = "Secondary navigation remains reachable in compact widths",
                testTag = "tabs-sub-scrollable",
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp),
                ) {
                    itemsIndexed(listOf("All", "Recent", "Favorites", "Shared", "Archived", "Downloads")) { index, label ->
                        SubTabItem(
                            onClick = { subSelected = index },
                            text = label,
                            selected = subSelected == index,
                        )
                    }
                }
            }
        }

        item {
            NavigationSample(
                title = "Main icon tabs",
                subtitle = "Auto-weighted icon destinations",
                testTag = "tabs-main-icon-auto-weight",
            ) {
                val icons = listOf(OneUiIcons.Home, OneUiIcons.Search, OneUiIcons.Share, OneUiIcons.Settings)
                Tabs(Modifier.fillMaxWidth()) {
                    icons.forEachIndexed { index, icon ->
                        CustomTabItem(
                            modifier = Modifier.weight(1f),
                            onClick = { iconSelected = index },
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        if (iconSelected == index) OneUiTheme.colors.accent.copy(alpha = 0.16f)
                                        else OneUiTheme.colors.surface,
                                    )
                                    .padding(horizontal = 22.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                OneUiIcon(
                                    icon = icon,
                                    contentDescription = "Icon tab ${index + 1}",
                                    modifier = Modifier.size(24.dp),
                                    tint = if (iconSelected == index) OneUiTheme.colors.accent else OneUiTheme.colors.secondaryText,
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            NavigationSample(
                title = "Bottom navigation with overflow",
                subtitle = "Four icon destinations plus More",
                testTag = "bottom-nav-icons-overflow",
            ) {
                val icons = listOf(
                    OneUiIcons.Home,
                    OneUiIcons.Search,
                    OneUiIcons.Share,
                    OneUiIcons.Settings,
                    OneUiIcons.More,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    icons.forEachIndexed { index, icon ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            OneUiIconButton(
                                icon = icon,
                                contentDescription = if (index == 4) "More" else "Destination ${index + 1}",
                                onClick = { bottomIconSelected = index },
                                tint = if (bottomIconSelected == index) OneUiTheme.colors.accent else OneUiTheme.colors.secondaryText,
                            )
                            Text(
                                text = if (index == 4) "More" else "Item ${index + 1}",
                                color = if (bottomIconSelected == index) OneUiTheme.colors.accent else OneUiTheme.colors.secondaryText,
                                fontSize = 11.sp,
                            )
                        }
                    }
                }
            }
        }

        item {
            NavigationSample(
                title = "Text bottom navigation",
                subtitle = "Text-only navigation destinations",
                testTag = "bottom-nav-text",
            ) {
                Tabs(Modifier.fillMaxWidth()) {
                    listOf("Home", "Library", "Discover", "Profile").forEachIndexed { index, label ->
                        SubTabItem(
                            modifier = Modifier.weight(1f),
                            onClick = { bottomTextSelected = index },
                            text = label,
                            selected = bottomTextSelected == index,
                        )
                    }
                }
            }
        }

        item {
            NavigationSample(
                title = "13 bottom tabs",
                subtitle = "Large destination set in a horizontally scrollable strip",
                testTag = "bottom-tabs-13-items",
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp),
                ) {
                    itemsIndexed((1..13).map { "Tab $it" }) { index, label ->
                        SubTabItem(
                            onClick = { manySelected = index },
                            text = label,
                            selected = manySelected == index,
                        )
                    }
                }
            }
        }
    }
}

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
