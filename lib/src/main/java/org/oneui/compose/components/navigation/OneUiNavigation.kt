package org.oneui.compose.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.interaction.oneUiInteractive
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.components.sheet.OneUiSheet
import org.oneui.compose.theme.OneUiTheme

@Immutable
data class OneUiNavigationItem(
    val id: String,
    val label: String,
    val icon: OneUiIcon? = null,
    val selectedIcon: OneUiIcon? = null,
)

/**
 * SESL tab treatments used by the reference catalog.
 *
 * [Rounded] is the filled pill selector, [Sub] is the centered scrollable sub-tab strip, and
 * [Main] is the weighted icon-and-label tab strip.
 */
enum class OneUiTabStyle {
    Rounded,
    Sub,
    Main,
}

@Composable
fun OneUiTabs(
    items: List<OneUiNavigationItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    showIcons: Boolean = false,
    style: OneUiTabStyle = OneUiTabStyle.Rounded,
) {
    require(items.isNotEmpty()) { "OneUiTabs requires at least one item" }
    val rowModifier = modifier
        .fillMaxWidth()
        .then(if (scrollable) Modifier.horizontalScroll(rememberScrollState()) else Modifier)
        .then(
            if (style == OneUiTabStyle.Rounded) {
                Modifier
                    .background(OneUiTheme.colors.tabRoundedBackground, RoundedCornerShape(20.dp))
                    .padding(2.dp)
            } else {
                Modifier
            },
        )
        .testTag("oneui-tabs")
    Row(
        modifier = rowModifier,
        horizontalArrangement = when {
            scrollable -> Arrangement.spacedBy(4.dp)
            style == OneUiTabStyle.Rounded -> Arrangement.spacedBy(2.dp)
            else -> Arrangement.SpaceEvenly
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, item ->
            OneUiTab(
                item = item,
                selected = index == selectedIndex,
                onClick = { onSelected(index) },
                showIcon = showIcons,
                style = style,
                modifier = if (scrollable) Modifier else Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun RowScope.OneUiTab(
    item: OneUiNavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
    showIcon: Boolean,
    style: OneUiTabStyle,
    modifier: Modifier = Modifier,
) {
    val selectedTextColor = when (style) {
        OneUiTabStyle.Rounded -> OneUiTheme.colors.tabRoundedSelectedText
        OneUiTabStyle.Sub -> OneUiTheme.colors.tabSubSelectedText
        OneUiTabStyle.Main -> OneUiTheme.colors.tabSelectedText
    }
    val unselectedTextColor = when (style) {
        OneUiTabStyle.Rounded -> OneUiTheme.colors.tabRoundedUnselectedText
        OneUiTabStyle.Sub -> OneUiTheme.colors.tabSubUnselectedText
        OneUiTabStyle.Main -> OneUiTheme.colors.tabUnselectedText
    }
    val color by animateColorAsState(
        targetValue = if (selected) selectedTextColor else unselectedTextColor,
        animationSpec = if (OneUiTheme.reducedMotion) androidx.compose.animation.core.snap() else OneUiMotion.navigationIndicator(),
        label = "One UI tab color",
    )
    Column(
        modifier = modifier
            .background(
                color = if (style == OneUiTabStyle.Rounded && selected) OneUiTheme.colors.accentStrong else Color.Transparent,
                shape = RoundedCornerShape(18.dp),
            )
            .semantics { this.selected = selected }
            .oneUiInteractive(
                enabled = true,
                onClick = onClick,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                role = Role.Tab,
                shape = RoundedCornerShape(18.dp),
                pressedScale = 0.98f,
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
        if (showIcon && (item.selectedIcon != null || item.icon != null)) {
            OneUiIcon(
                icon = if (selected) item.selectedIcon ?: item.icon!! else item.icon ?: item.selectedIcon!!,
                contentDescription = item.label,
                modifier = Modifier.size(22.dp),
                tint = color,
            )
        }
        Text(
            item.label,
            color = color,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
        )
        if (style != OneUiTabStyle.Rounded) {
            Box(
                modifier = Modifier
                    .background(
                        if (selected) {
                            if (style == OneUiTabStyle.Sub) OneUiTheme.colors.tabSubSelectedText
                            else OneUiTheme.colors.tabIndicator
                        } else {
                            Color.Transparent
                        },
                        RoundedCornerShape(2.dp),
                    )
                    .fillMaxWidth()
                    .padding(vertical = if (style == OneUiTabStyle.Sub) 1.dp else 1.5.dp),
            )
        } else {
            Spacer(Modifier.height(3.dp))
        }
    }
}

@Composable
fun OneUiBottomNavigation(
    items: List<OneUiNavigationItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxVisibleItems: Int = 5,
    showIcons: Boolean = true,
) {
    require(items.isNotEmpty()) { "OneUiBottomNavigation requires at least one item" }
    require(maxVisibleItems >= 2) { "maxVisibleItems must be at least 2" }
    val hasOverflow = items.size > maxVisibleItems
    val visibleCount = if (hasOverflow) maxVisibleItems - 1 else items.size
    val visible = items.take(visibleCount)
    val overflow = items.drop(visible.size)
    val more = OneUiNavigationItem("__more__", "More", OneUiIcons.More)
    var overflowVisible by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(OneUiTheme.colors.surfaceElevated)
            .testTag("oneui-bottom-navigation")
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        visible.forEachIndexed { index, item ->
            OneUiBottomNavigationItem(
                item = item,
                selected = index == selectedIndex,
                onClick = { onSelected(index) },
                showIcon = showIcons,
            )
        }
        if (hasOverflow) {
            OneUiBottomNavigationItem(
                item = more,
                selected = selectedIndex >= visible.size,
                onClick = { overflowVisible = true },
                showIcon = showIcons,
                modifier = Modifier.testTag("oneui-bottom-navigation-overflow"),
            )
        }
    }

    OneUiNavigationOverflowSheet(
        visible = overflowVisible,
        items = overflow,
        firstItemIndex = visible.size,
        selectedIndex = selectedIndex,
        onDismissRequest = { overflowVisible = false },
        onSelected = { index ->
            overflowVisible = false
            onSelected(index)
        },
    )
}

/**
 * Compose-native equivalent of SESL8 [BottomTabLayout]. It keeps the first destinations in the
 * bottom bar and exposes the remaining destinations through the reference-style grid sheet.
 */
@Composable
fun OneUiBottomTabLayout(
    items: List<OneUiNavigationItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visibleItemCount: Int = 3,
) {
    require(items.isNotEmpty()) { "OneUiBottomTabLayout requires at least one item" }
    require(visibleItemCount > 0) { "visibleItemCount must be positive" }

    val visible = items.take(visibleItemCount)
    val overflow = items.drop(visible.size)
    var overflowVisible by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(OneUiTheme.colors.surfaceElevated)
            .testTag("oneui-bottom-tab-layout")
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        visible.forEachIndexed { index, item ->
            OneUiBottomNavigationItem(
                item = item,
                selected = selectedIndex == index,
                onClick = { onSelected(index) },
            )
        }
        if (overflow.isNotEmpty()) {
            OneUiBottomNavigationItem(
                item = OneUiNavigationItem("__more__", "More", OneUiIcons.More),
                selected = selectedIndex >= visible.size,
                onClick = { overflowVisible = true },
                modifier = Modifier.testTag("oneui-bottom-tab-overflow"),
            )
        }
    }

    OneUiNavigationOverflowSheet(
        visible = overflowVisible,
        items = overflow,
        firstItemIndex = visible.size,
        selectedIndex = selectedIndex,
        onDismissRequest = { overflowVisible = false },
        onSelected = { index ->
            overflowVisible = false
            onSelected(index)
        },
    )
}

@Composable
private fun OneUiNavigationOverflowSheet(
    visible: Boolean,
    items: List<OneUiNavigationItem>,
    firstItemIndex: Int,
    selectedIndex: Int,
    onDismissRequest: () -> Unit,
    onSelected: (Int) -> Unit,
) {
    if (items.isEmpty()) return
    OneUiSheet(
        visible = visible,
        onDismissRequest = onDismissRequest,
        title = "More",
        modifier = Modifier.testTag("oneui-navigation-overflow-sheet"),
    ) {
        items.chunked(3).forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                rowItems.forEachIndexed { columnIndex, item ->
                    val itemIndex = firstItemIndex + rowIndex * 3 + columnIndex
                    OneUiBottomNavigationItem(
                        item = item,
                        selected = selectedIndex == itemIndex,
                        onClick = { onSelected(itemIndex) },
                        showIcon = true,
                    )
                }
                repeat(3 - rowItems.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun RowScope.OneUiBottomNavigationItem(
    item: OneUiNavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
    showIcon: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val tint by animateColorAsState(
        targetValue = if (selected) OneUiTheme.colors.accent else OneUiTheme.colors.secondaryText,
        animationSpec = if (OneUiTheme.reducedMotion) androidx.compose.animation.core.snap() else OneUiMotion.navigationIndicator(),
        label = "One UI bottom navigation tint",
    )
    Column(
        modifier = modifier
            .weight(1f)
            .semantics { this.selected = selected }
            .oneUiInteractive(
                enabled = true,
                onClick = onClick,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                role = Role.Tab,
                shape = RoundedCornerShape(18.dp),
            )
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        (if (showIcon) (if (selected) item.selectedIcon ?: item.icon else item.icon) else null)?.let {
            OneUiIcon(
                icon = it,
                contentDescription = item.label,
                modifier = Modifier.size(24.dp),
                tint = tint,
            )
        }
        Text(item.label, color = tint, fontSize = 11.sp)
    }
}

@Composable
fun OneUiNavigationRail(
    items: List<OneUiNavigationItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(OneUiTheme.colors.surfaceElevated)
            .testTag("oneui-navigation-rail")
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items.forEachIndexed { index, item ->
            val tint = if (index == selectedIndex) OneUiTheme.colors.accent else OneUiTheme.colors.secondaryText
            OneUiIconButton(
                icon = item.icon ?: OneUiIcons.Info,
                contentDescription = item.label,
                onClick = { onSelected(index) },
                tint = tint,
            )
        }
    }
}
