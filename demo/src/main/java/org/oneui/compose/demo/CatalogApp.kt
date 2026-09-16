package org.oneui.compose.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.oneui.compose.demo.screens.ParityStatusScreen
import org.oneui.compose.demo.screens.PickersScreen
import org.oneui.compose.demo.screens.ProgressBarsScreen
import org.oneui.compose.demo.screens.RecyclerViewsScreen
import org.oneui.compose.demo.screens.ReferencePlaceholderScreen
import org.oneui.compose.demo.screens.SeekBarsScreen
import org.oneui.compose.demo.screens.WidgetsScreen
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.patterns.shell.OneUiAppShell

@Composable
fun CatalogApp(
    onOpenLegacyShowcase: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedName by rememberSaveable { mutableStateOf(CatalogDestination.ProgressBars.name) }
    val selected = CatalogDestination.entries.firstOrNull { it.name == selectedName }
        ?: CatalogDestination.ProgressBars

    OneUiAppShell(
        destinations = CatalogDestination.drawerEntries.map(CatalogDestination::asShellDestination),
        selectedId = selected.name,
        onDestinationSelected = { id ->
            val destination = CatalogDestination.entries.firstOrNull { it.name == id } ?: return@OneUiAppShell
            if (destination == CatalogDestination.LegacyShowcase) {
                onOpenLegacyShowcase()
            } else {
                selectedName = destination.name
            }
        },
        title = selected.label,
        subtitle = if (selected.reference) "OneUI8 sample parity" else "Implementation diagnostics",
        modifier = modifier,
        headerAction = {
            OneUiIconButton(
                icon = OneUiIcons.Settings,
                contentDescription = "Preferences",
                onClick = { selectedName = CatalogDestination.Preferences.name },
            )
        },
        actions = {
            OneUiIconButton(
                icon = OneUiIcons.More,
                contentDescription = "Parity status",
                onClick = { selectedName = CatalogDestination.ParityStatus.name },
            )
        },
    ) {
        when (selected) {
            CatalogDestination.ProgressBars -> ProgressBarsScreen()
            CatalogDestination.SeekBars -> SeekBarsScreen()
            CatalogDestination.Widgets -> WidgetsScreen()
            CatalogDestination.ParityStatus -> ParityStatusScreen()
            CatalogDestination.Pickers -> PickersScreen()
            CatalogDestination.QrCodes -> ReferencePlaceholderScreen(
                title = "QRCodes",
                testTag = selected.testTag,
                referenceScope = "Four QR presentation variants plus QR-in-bottom-sheet/profile actions.",
            )
            CatalogDestination.Navigation -> ReferencePlaceholderScreen(
                title = "Navigation",
                testTag = selected.testTag,
                referenceScope = "Rounded tabs, subtabs, icon tabs, bottom navigation, bottom tabs, navigation rail and drawer modes.",
            )
            CatalogDestination.RecyclerViews -> RecyclerViewsScreen()
            CatalogDestination.CustomAbout -> ReferencePlaceholderScreen(
                title = "Custom About",
                testTag = selected.testTag,
                referenceScope = "Adaptive collapsing identity header, contributors, licenses, related links and predictive-back behavior.",
            )
            CatalogDestination.Preferences -> ReferencePlaceholderScreen(
                title = "Preferences",
                testTag = selected.testTag,
                referenceScope = "Complete app_preferences.xml parity including cards, switch/radio/check/list/color/seekbar variants and dependencies.",
            )
            CatalogDestination.About -> ReferencePlaceholderScreen(
                title = "About",
                testTag = selected.testTag,
                referenceScope = "Compose-native AppInfo layout with status and project actions.",
            )
            CatalogDestination.LegacyShowcase -> Unit
        }
    }
}
