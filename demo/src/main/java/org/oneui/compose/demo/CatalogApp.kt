package org.oneui.compose.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.oneui.compose.demo.screens.NavigationScreen
import org.oneui.compose.demo.screens.AboutScreen
import org.oneui.compose.demo.screens.CustomAboutScreen
import org.oneui.compose.demo.screens.ParityStatusScreen
import org.oneui.compose.demo.screens.PickersScreen
import org.oneui.compose.demo.screens.PreferencesScreen
import org.oneui.compose.demo.screens.ProgressBarsScreen
import org.oneui.compose.demo.screens.QrCodesScreen
import org.oneui.compose.demo.screens.RecyclerViewsScreen
import org.oneui.compose.demo.screens.SeekBarsScreen
import org.oneui.compose.demo.screens.WidgetsScreen
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.patterns.shell.OneUiAppShell

internal enum class CatalogThemeMode(
    val label: String,
    val darkThemeOverride: Boolean?,
) {
    System("System default", null),
    Light("Light", false),
    Dark("Dark", true),
}

@Composable
fun CatalogApp(
    onOpenLegacyShowcase: () -> Unit,
    modifier: Modifier = Modifier,
    themeMode: CatalogThemeMode = CatalogThemeMode.System,
    onThemeModeChange: (CatalogThemeMode) -> Unit = {},
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
            CatalogDestination.QrCodes -> QrCodesScreen()
            CatalogDestination.Navigation -> NavigationScreen()
            CatalogDestination.RecyclerViews -> RecyclerViewsScreen()
            CatalogDestination.CustomAbout -> CustomAboutScreen()
            CatalogDestination.Preferences -> PreferencesScreen(
                onOpenAbout = { selectedName = CatalogDestination.About.name },
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange,
            )
            CatalogDestination.About -> AboutScreen()
            CatalogDestination.LegacyShowcase -> Unit
        }
    }
}
