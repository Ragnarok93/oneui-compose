package org.oneui.compose.oneui8.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.buttons.OneUiProgressButton
import org.oneui.compose.components.feedback.OneUiBottomTip
import org.oneui.compose.components.feedback.OneUiSnackbar
import org.oneui.compose.components.feedback.OneUiTipPopup
import org.oneui.compose.components.input.OneUiSpinner
import org.oneui.compose.components.input.OneUiTextField
import org.oneui.compose.components.list.OneUiSwipeAction
import org.oneui.compose.components.list.OneUiSwipeActionRow
import org.oneui.compose.components.list.OneUiSwipeDirection
import org.oneui.compose.components.list.OneUiRadioItem
import org.oneui.compose.components.list.OneUiSwitchItem
import org.oneui.compose.components.menu.OneUiMenu
import org.oneui.compose.components.menu.OneUiMenuItem
import org.oneui.compose.components.navigation.OneUiBottomNavigation
import org.oneui.compose.components.navigation.OneUiBottomTabLayout
import org.oneui.compose.components.navigation.OneUiNavigationItem
import org.oneui.compose.components.navigation.OneUiNavigationRail
import org.oneui.compose.components.navigation.OneUiTabs
import org.oneui.compose.components.preference.OneUiPreference
import org.oneui.compose.components.preference.OneUiPreferenceCategory
import org.oneui.compose.components.preference.OneUiSuggestionCard
import org.oneui.compose.components.preference.OneUiTipsCard
import org.oneui.compose.components.qr.OneUiQrCode
import org.oneui.compose.components.slider.OneUiLevelSlider
import org.oneui.compose.components.slider.OneUiSeekBarPlus
import org.oneui.compose.components.surface.OneUiSurface
import org.oneui.compose.components.surface.OneUiSurfaceBox
import org.oneui.compose.components.menu.OneUiMenuItemRow
import org.oneui.compose.patterns.about.OneUiAboutLink
import org.oneui.compose.patterns.about.OneUiAppInfo
import org.oneui.compose.patterns.about.OneUiAppInfoStatus
import org.oneui.compose.patterns.about.OneUiCustomAbout
import org.oneui.compose.patterns.appbar.OneUiActionMode
import org.oneui.compose.patterns.appbar.OneUiActionModeAction
import org.oneui.compose.patterns.appbar.OneUiSearchMode
import org.oneui.compose.patterns.appbar.OneUiSearchModeBehavior
import org.oneui.compose.patterns.appbar.OneUiSearchModeState
import org.oneui.compose.patterns.appbar.rememberOneUiSearchModeState
import org.oneui.compose.patterns.cards.OneUiRelatedLink
import org.oneui.compose.patterns.cards.OneUiRelatedLinksCard
import org.oneui.compose.patterns.preferences.OneUiPreferencesScreen
import org.oneui.compose.theme.OneUiTheme

typealias OneUI8MenuItem = OneUiMenuItem
typealias OneUI8SearchModeBehavior = OneUiSearchModeBehavior
typealias OneUI8SearchModeState = OneUiSearchModeState
typealias OneUI8ActionModeAction = OneUiActionModeAction
typealias OneUI8AboutLink = OneUiAboutLink
typealias OneUI8AppInfoStatus = OneUiAppInfoStatus
typealias OneUI8RelatedLink = OneUiRelatedLink
typealias OneUI8SwipeAction = OneUiSwipeAction
typealias OneUI8SwipeDirection = OneUiSwipeDirection

@Composable
fun OneUI8Menu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<OneUI8MenuItem>,
    onItemClick: (OneUI8MenuItem) -> Unit,
    modifier: Modifier = Modifier,
) = OneUiMenu(expanded, onDismissRequest, items, onItemClick, modifier)

@Composable
fun OneUI8Spinner(
    selectedIndex: Int,
    entries: List<String>,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) = OneUiSpinner(selectedIndex, entries, onSelected, modifier, enabled)

@Composable
fun OneUI8TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    hint: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
) = OneUiTextField(value, onValueChange, modifier, label, hint, enabled, singleLine)

@Composable
fun OneUI8MenuItemRow(
    item: OneUI8MenuItem,
    modifier: Modifier = Modifier,
) = OneUiMenuItemRow(item, modifier)

@Composable
fun OneUI8SwitchItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    enabled: Boolean = true,
) = OneUiSwitchItem(title, checked, onCheckedChange, modifier, summary, enabled)

@Composable
fun OneUI8RadioItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    enabled: Boolean = true,
) = OneUiRadioItem(title, selected, onClick, modifier, summary, enabled)

@Composable
fun OneUI8SwipeActionRow(
    leftAction: OneUI8SwipeAction,
    rightAction: OneUI8SwipeAction,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    threshold: androidx.compose.ui.unit.Dp = 64.dp,
    maxReveal: androidx.compose.ui.unit.Dp = 96.dp,
    content: @Composable () -> Unit,
) = OneUiSwipeActionRow(
    leftAction = leftAction,
    rightAction = rightAction,
    onSwipeLeft = onSwipeLeft,
    onSwipeRight = onSwipeRight,
    modifier = modifier,
    enabled = enabled,
    threshold = threshold,
    maxReveal = maxReveal,
    content = content,
)

@Composable
fun OneUI8Tabs(
    items: List<OneUiNavigationItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    showIcons: Boolean = false,
) = OneUiTabs(items, selectedIndex, onSelected, modifier, scrollable, showIcons)

@Composable
fun OneUI8BottomNavigation(
    items: List<OneUiNavigationItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxVisibleItems: Int = 5,
) = OneUiBottomNavigation(items, selectedIndex, onSelected, modifier, maxVisibleItems)

@Composable
fun OneUI8BottomTabLayout(
    items: List<OneUiNavigationItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visibleItemCount: Int = 3,
) = OneUiBottomTabLayout(items, selectedIndex, onSelected, modifier, visibleItemCount)

@Composable
fun OneUI8NavigationRail(
    items: List<OneUiNavigationItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) = OneUiNavigationRail(items, selectedIndex, onSelected, modifier)

@Composable
fun OneUI8Surface(
    modifier: Modifier = Modifier,
    containerColor: Color = OneUiTheme.colors.surface,
    shape: Shape = OneUiTheme.shapes.card,
    content: @Composable ColumnScope.() -> Unit,
) = OneUiSurface(modifier, containerColor, shape, content)

@Composable
fun OneUI8SurfaceBox(
    modifier: Modifier = Modifier,
    containerColor: Color = OneUiTheme.colors.surface,
    shape: Shape = androidx.compose.foundation.shape.RoundedCornerShape(22.dp),
    content: @Composable () -> Unit,
) = OneUiSurfaceBox(modifier, containerColor, shape, content)

@Composable
fun OneUI8ProgressButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    progress: Float? = null,
    enabled: Boolean = true,
) = OneUiProgressButton(label, onClick, modifier, progress, enabled)

@Composable
fun OneUI8LevelSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    seamless: Boolean = false,
    showTickMark: Boolean = false,
    enabled: Boolean = true,
) = OneUiLevelSlider(value, onValueChange, valueRange, modifier, seamless, showTickMark, enabled)

@Composable
fun OneUI8SeekBarPlus(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    seamless: Boolean = true,
    enabled: Boolean = true,
) = OneUiSeekBarPlus(value, onValueChange, valueRange, modifier, seamless, enabled)

@Composable
fun OneUI8SearchMode(
    active: Boolean,
    query: String,
    onActiveChange: (Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Search",
    behavior: OneUI8SearchModeBehavior = OneUI8SearchModeBehavior.Dismiss,
    onSearch: (() -> Unit)? = null,
) = OneUiSearchMode(active, query, onActiveChange, onQueryChange, modifier, hint, behavior, onSearch)

@Composable
fun rememberOneUI8SearchModeState(
    behavior: OneUI8SearchModeBehavior = OneUI8SearchModeBehavior.Dismiss,
): MutableState<OneUI8SearchModeState> = rememberOneUiSearchModeState(behavior)

@Composable
fun OneUI8ActionMode(
    selectedCount: Int,
    allSelected: Boolean,
    onSelectAll: () -> Unit,
    onCancel: () -> Unit,
    actions: List<OneUI8ActionModeAction>,
    onAction: (OneUI8ActionModeAction) -> Unit,
    modifier: Modifier = Modifier,
    showCancelButton: Boolean = false,
    visible: Boolean = true,
) = OneUiActionMode(selectedCount, allSelected, onSelectAll, onCancel, actions, onAction, modifier, showCancelButton, visible)

@Composable
fun OneUI8Snackbar(
    visible: Boolean,
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) = OneUiSnackbar(visible, message, modifier, actionLabel, onAction)

@Composable
fun OneUI8TipPopup(
    visible: Boolean,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
) = OneUiTipPopup(visible, title, message, modifier, onDismiss)

@Composable
fun OneUI8BottomTip(
    title: String,
    summary: String,
    linkLabel: String,
    onLinkClick: () -> Unit,
    modifier: Modifier = Modifier,
) = OneUiBottomTip(title, summary, linkLabel, onLinkClick, modifier)

@Composable
fun OneUI8SuggestionCard(
    title: String,
    summary: String,
    actionLabel: String,
    onAction: () -> Unit,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) = OneUiSuggestionCard(title, summary, actionLabel, onAction, onDismiss, modifier)

@Composable
fun OneUI8TipsCard(
    title: String,
    summary: String,
    buttonLabel: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) = OneUiTipsCard(title, summary, buttonLabel, onButtonClick, modifier)

@Composable
fun OneUI8Preference(
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
) = OneUiPreference(title, modifier, summary, enabled, onClick, trailing)

@Composable
fun OneUI8PreferenceCategory(
    title: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) = OneUiPreferenceCategory(title, modifier, content)

@Composable
fun OneUI8PreferencesScreen(
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit,
) = OneUiPreferencesScreen(modifier, content)

@Composable
fun OneUI8RelatedLinksCard(
    title: String,
    links: List<OneUI8RelatedLink>,
    modifier: Modifier = Modifier,
) = OneUiRelatedLinksCard(title, links, modifier)

@Composable
fun OneUI8AppInfo(
    appName: String,
    version: String,
    status: OneUI8AppInfoStatus,
    onStatusAction: () -> Unit,
    onGithubClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    extraInfo: String? = null,
) = OneUiAppInfo(appName, version, status, onStatusAction, onGithubClick, modifier, onBack, extraInfo)

@Composable
fun OneUI8CustomAbout(
    appName: String,
    version: String,
    contributors: List<String>,
    licenses: List<String>,
    links: List<OneUI8AboutLink>,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    backProgress: Float = 0f,
    appIcon: org.oneui.compose.icons.OneUiIcon = org.oneui.compose.icons.OneUiIcons.Info,
    onGithubClick: () -> Unit = {},
    onTelegramClick: () -> Unit = {},
    onAppInfoClick: () -> Unit = {},
) = OneUiCustomAbout(
    appName = appName,
    version = version,
    contributors = contributors,
    licenses = licenses,
    links = links,
    modifier = modifier,
    onBack = onBack,
    backProgress = backProgress,
    appIcon = appIcon,
    onGithubClick = onGithubClick,
    onTelegramClick = onTelegramClick,
    onAppInfoClick = onAppInfoClick,
)

@Composable
fun OneUI8QrCode(
    data: String,
    modifier: Modifier = Modifier,
    foregroundColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    contentDescription: String? = "QR code",
) = OneUiQrCode(data, modifier, foregroundColor, backgroundColor, contentDescription = contentDescription)
