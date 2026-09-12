package org.oneui.compose.patterns.shell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.interaction.oneUiInteractive
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.patterns.appbar.OneUiLargeTitleAppBar
import org.oneui.compose.theme.OneUiTheme

@Immutable
data class OneUiAppShellDestination(
    val id: String,
    val label: String,
    val icon: OneUiIcon,
)

/**
 * One UI Compose application shell with compact overlay-drawer and expanded permanent-drawer
 * presentations. This is a Compose-native counterpart to the sample NavDrawerLayout shell.
 */
@Composable
fun OneUiAppShell(
    destinations: List<OneUiAppShellDestination>,
    selectedId: String,
    onDestinationSelected: (String) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    headerAction: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    require(destinations.isNotEmpty()) { "OneUiAppShell requires at least one destination" }
    var compactDrawerOpen by rememberSaveable { mutableStateOf(false) }
    val colors = OneUiTheme.colors

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
    ) {
        val expanded = maxWidth >= 840.dp
        if (expanded) {
            Row(Modifier.fillMaxSize()) {
                DrawerPanel(
                    destinations = destinations,
                    selectedId = selectedId,
                    onDestinationSelected = onDestinationSelected,
                    headerAction = headerAction,
                    modifier = Modifier
                        .width(292.dp)
                        .fillMaxHeight(),
                )
                Column(Modifier.weight(1f)) {
                    OneUiLargeTitleAppBar(
                        title = title,
                        subtitle = subtitle,
                        actions = actions,
                    )
                    Box(Modifier.fillMaxSize()) { content() }
                }
            }
        } else {
            Box(Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxSize()) {
                    OneUiLargeTitleAppBar(
                        title = title,
                        subtitle = subtitle,
                        navigationIcon = OneUiIcons.Grid,
                        navigationContentDescription = "Open navigation",
                        onNavigationClick = { compactDrawerOpen = true },
                        actions = actions,
                    )
                    Box(Modifier.fillMaxSize()) { content() }
                }

                AnimatedVisibility(
                    visible = compactDrawerOpen,
                    enter = fadeIn(animationSpec = OneUiMotion.quick()),
                    exit = fadeOut(animationSpec = OneUiMotion.quick()),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.32f))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { compactDrawerOpen = false },
                    )
                }
                AnimatedVisibility(
                    visible = compactDrawerOpen,
                    enter = slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = OneUiMotion.standard(),
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = OneUiMotion.standard(),
                    ),
                    modifier = Modifier.align(Alignment.CenterStart),
                ) {
                    DrawerPanel(
                        destinations = destinations,
                        selectedId = selectedId,
                        onDestinationSelected = { id ->
                            onDestinationSelected(id)
                            compactDrawerOpen = false
                        },
                        headerAction = headerAction,
                        modifier = Modifier
                            .width(maxWidth.coerceAtMost(328.dp))
                            .fillMaxHeight(),
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerPanel(
    destinations: List<OneUiAppShellDestination>,
    selectedId: String,
    onDestinationSelected: (String) -> Unit,
    headerAction: (@Composable () -> Unit)?,
    modifier: Modifier,
) {
    val colors = OneUiTheme.colors
    val interactionShape = RoundedCornerShape(22.dp)
    Column(
        modifier = modifier
            .background(colors.surfaceElevated)
            .padding(horizontal = 12.dp, vertical = 18.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "OneUI Compose 8",
                    color = colors.primaryText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Sample parity catalog",
                    color = colors.secondaryText,
                    fontSize = 13.sp,
                )
            }
            if (headerAction != null) headerAction()
        }
        Spacer(Modifier.size(6.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            destinations.forEach { destination ->
                val selected = destination.id == selectedId
                val interactionSource = remember(destination.id) { MutableInteractionSource() }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (selected) colors.accent.copy(alpha = 0.14f) else Color.Transparent,
                            interactionShape,
                        )
                        .oneUiInteractive(
                            enabled = true,
                            onClick = { onDestinationSelected(destination.id) },
                            interactionSource = interactionSource,
                            role = Role.Button,
                            shape = interactionShape,
                            pressedScale = 0.985f,
                        )
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    org.oneui.compose.icons.OneUiIcon(
                        icon = destination.icon,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = if (selected) colors.accent else colors.secondaryText,
                    )
                    Text(
                        text = destination.label,
                        color = if (selected) colors.accent else colors.primaryText,
                        fontSize = 15.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                    )
                }
            }
        }
    }
}
