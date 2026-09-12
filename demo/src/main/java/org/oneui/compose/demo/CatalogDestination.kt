package org.oneui.compose.demo

import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.patterns.shell.OneUiAppShellDestination

enum class CatalogDestination(
    val label: String,
    val testTag: String,
    val icon: OneUiIcon,
    val reference: Boolean = true,
    val showInDrawer: Boolean = true,
) {
    ProgressBars("ProgressBars", "catalog-progress-bars", OneUiIcons.Motion),
    SeekBars("SeekBars", "catalog-seek-bars", OneUiIcons.Settings),
    Pickers("Pickers", "catalog-pickers", OneUiIcons.Check),
    QrCodes("QRCodes", "catalog-qr-codes", OneUiIcons.Grid),
    Navigation("Navigation", "catalog-navigation", OneUiIcons.Home),
    RecyclerViews("RecyclerViews", "catalog-recycler-views", OneUiIcons.Grid),
    Widgets("Misc. Widgets", "catalog-misc-widgets", OneUiIcons.Add),
    CustomAbout("Custom About", "catalog-custom-about", OneUiIcons.Info),
    Preferences("Preferences", "catalog-preferences", OneUiIcons.Settings, showInDrawer = false),
    About("About", "catalog-about", OneUiIcons.Info, showInDrawer = false),
    LegacyShowcase("Legacy showcase", "catalog-legacy-showcase", OneUiIcons.Motion, reference = false),
    ParityStatus("Parity status", "catalog-parity-status", OneUiIcons.Check, reference = false),
    ;

    fun asShellDestination() = OneUiAppShellDestination(
        id = name,
        label = label,
        icon = icon,
    )

    companion object {
        val drawerEntries: List<CatalogDestination> = entries.filter { it.showInDrawer }
    }
}
