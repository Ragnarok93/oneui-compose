package org.oneui.compose.patterns.about

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import org.oneui.compose.components.list.OneUiListItem
import org.oneui.compose.components.surface.OneUiSurface
import org.oneui.compose.components.surface.OneUiSurfaceBox
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.theme.OneUiTheme
import kotlin.math.max

data class OneUiAboutLink(
    val label: String,
    val onClick: () -> Unit,
)

/**
 * Compose-native equivalent of the SESL8 custom-about page.
 *
 * The expanded header occupies half of a portrait window, collapses with the same 300 ms One UI
 * app-bar motion, and changes to a pinned compact toolbar as the content is scrolled. Optional
 * callbacks are appended to the API so existing callers remain source-compatible.
 */
@Composable
fun OneUiCustomAbout(
    appName: String,
    version: String,
    contributors: List<String>,
    licenses: List<String>,
    links: List<OneUiAboutLink>,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    backProgress: Float = 0f,
    appIcon: OneUiIcon = OneUiIcons.Info,
    onGithubClick: () -> Unit = {},
    onTelegramClick: () -> Unit = {},
    onAppInfoClick: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val portrait = configuration.orientation != android.content.res.Configuration.ORIENTATION_LANDSCAPE

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val expandedHeight = if (portrait) {
            if (maxHeight == Dp.Infinity) 360.dp else (maxHeight * 0.5f).coerceAtLeast(320.dp)
        } else {
            0.dp
        }
        val expandedHeightPx = with(androidx.compose.ui.platform.LocalDensity.current) {
            expandedHeight.toPx().coerceAtLeast(1f)
        }
        val scrollFraction by remember(listState, expandedHeightPx) {
            derivedStateOf {
                if (expandedHeight == 0.dp) {
                    1f
                } else {
                    val consumed = if (listState.firstVisibleItemIndex > 0) {
                        expandedHeightPx
                    } else {
                        listState.firstVisibleItemScrollOffset.toFloat()
                    }
                    (consumed / expandedHeightPx).coerceIn(0f, 1f)
                }
            }
        }
        val targetFraction = max(scrollFraction, backProgress.coerceIn(0f, 1f))
        val collapseFraction by animateFloatAsState(
            targetValue = targetFraction,
            animationSpec = if (OneUiTheme.reducedMotion) snap() else OneUiMotion.appBarCollapse(),
            label = "One UI custom about collapse",
        )
        val toolbarColor by animateColorAsState(
            targetValue = OneUiTheme.colors.background.copy(alpha = if (collapseFraction > 0.5f) 1f else 0f),
            animationSpec = if (OneUiTheme.reducedMotion) snap() else OneUiMotion.appBarCollapse(),
            label = "One UI custom about toolbar color",
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("oneui-custom-about"),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = listState,
        ) {
            if (expandedHeight > 0.dp) {
                item {
                    OneUiCustomAboutHeader(
                        appName = appName,
                        version = version,
                        appIcon = appIcon,
                        expandedHeight = expandedHeight,
                        collapsedFraction = collapseFraction,
                        onGithubClick = onGithubClick,
                        onTelegramClick = onTelegramClick,
                    )
                }
            }
            item {
                OneUiAboutIdentity(
                    appName = appName,
                    version = version,
                    appIcon = appIcon,
                )
            }
            item {
                OneUiAboutSection(title = "OneUI Project Team") {
                    contributors.forEach { name ->
                        OneUiListItem(
                            title = name,
                            leading = {
                                OneUiIcon(
                                    icon = appIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(38.dp),
                                    tint = OneUiTheme.colors.accent,
                                )
                            },
                        )
                    }
                }
            }
            item {
                OneUiAboutSection(title = "Open source licenses") {
                    licenses.forEach { license -> OneUiListItem(title = license) }
                }
            }
            item {
                OneUiAboutSection(title = "Useful links") {
                    links.forEach { link -> OneUiListItem(title = link.label, onClick = link.onClick) }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(toolbarColor)
                .zIndex(2f)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (onBack != null) {
                OneUiIconButton(
                    icon = OneUiIcons.Back,
                    contentDescription = "Back",
                    onClick = onBack,
                )
            }
            AnimatedVisibility(
                visible = collapseFraction > 0.5f,
                enter = if (OneUiTheme.reducedMotion) EnterTransition.None else fadeIn(animationSpec = OneUiMotion.appBarCollapse()),
                exit = if (OneUiTheme.reducedMotion) ExitTransition.None else fadeOut(animationSpec = OneUiMotion.appBarCollapse()),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = appName,
                    color = OneUiTheme.colors.primaryText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            if (onBack == null) Spacer(Modifier.weight(1f))
            OneUiIconButton(
                icon = OneUiIcons.Info,
                contentDescription = "App info",
                onClick = onAppInfoClick,
            )
        }

        if (portrait && expandedHeight > 0.dp) {
            AnimatedVisibility(
                visible = collapseFraction < 0.5f,
                enter = if (OneUiTheme.reducedMotion) EnterTransition.None else fadeIn(animationSpec = OneUiMotion.appBarCollapse()),
                exit = if (OneUiTheme.reducedMotion) ExitTransition.None else fadeOut(animationSpec = OneUiMotion.appBarCollapse()),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 18.dp),
            ) {
                OneUiIconButton(
                    icon = OneUiIcons.ArrowUp,
                    contentDescription = "Swipe up to expand",
                    onClick = {
                        scope.launch {
                            if (OneUiTheme.reducedMotion) listState.scrollToItem(1)
                            else listState.animateScrollToItem(1)
                        }
                    },
                    tint = OneUiTheme.colors.secondaryText.copy(alpha = 0.65f),
                )
            }
        }
    }
}

@Composable
private fun OneUiCustomAboutHeader(
    appName: String,
    version: String,
    appIcon: OneUiIcon,
    expandedHeight: Dp,
    collapsedFraction: Float,
    onGithubClick: () -> Unit,
    onTelegramClick: () -> Unit,
) {
    val fraction = collapsedFraction.coerceIn(0f, 1f)
    OneUiSurfaceBox(
        modifier = Modifier
            .fillMaxWidth()
            .height(expandedHeight)
            .testTag("oneui-custom-about-header"),
        containerColor = OneUiTheme.colors.background,
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp, bottom = 20.dp)
                .alpha((1f - fraction * 0.9f).coerceIn(0.1f, 1f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            OneUiIcon(
                icon = appIcon,
                contentDescription = "Application icon",
                modifier = Modifier.size((72f - 34f * fraction).dp),
                tint = OneUiTheme.colors.accent,
            )
            Text(
                text = appName,
                modifier = Modifier.padding(top = 8.dp),
                color = OneUiTheme.colors.primaryText,
                fontSize = (30f - 12f * fraction).sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Version $version",
                modifier = Modifier.padding(top = 8.dp),
                color = OneUiTheme.colors.secondaryText,
                fontSize = 14.sp,
            )
            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OneUiIconButton(
                    icon = OneUiIcons.Website,
                    contentDescription = "GitHub",
                    onClick = onGithubClick,
                    enabled = fraction < 0.5f,
                )
                OneUiIconButton(
                    icon = OneUiIcons.Message,
                    contentDescription = "Telegram",
                    onClick = onTelegramClick,
                    enabled = fraction < 0.5f,
                )
            }
        }
    }
}

@Composable
private fun OneUiAboutIdentity(
    appName: String,
    version: String,
    appIcon: OneUiIcon,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OneUiIcon(
            icon = appIcon,
            contentDescription = null,
            modifier = Modifier.size(38.dp),
            tint = OneUiTheme.colors.accent,
        )
        Column(
            modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 10.dp),
        ) {
            Text(appName, color = OneUiTheme.colors.primaryText, fontSize = 18.sp)
            Text(version, color = OneUiTheme.colors.secondaryText, fontSize = 14.sp)
        }
    }
}

@Composable
private fun OneUiAboutSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(title, color = OneUiTheme.colors.secondaryText, style = OneUiTheme.typography.sectionLabel)
        OneUiSurface(modifier = Modifier.fillMaxWidth()) { content() }
    }
}
