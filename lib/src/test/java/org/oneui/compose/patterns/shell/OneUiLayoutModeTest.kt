package org.oneui.compose.patterns.shell

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class OneUiLayoutModeTest {
    @Test
    fun width840AndHeight480SelectsDesktop() {
        assertEquals(
            OneUiLayoutMode.Desktop,
            OneUiLayoutMode.fromWindow(width = 840.dp, height = 480.dp),
        )
    }

    @Test
    fun width720AndHeight600SelectsDesktop() {
        assertEquals(
            OneUiLayoutMode.Desktop,
            OneUiLayoutMode.fromWindow(width = 720.dp, height = 600.dp),
        )
    }

    @Test
    fun width839OrHeight479RemainsCompact() {
        assertEquals(
            OneUiLayoutMode.Compact,
            OneUiLayoutMode.fromWindow(width = 839.dp, height = 480.dp),
        )
        assertEquals(
            OneUiLayoutMode.Compact,
            OneUiLayoutMode.fromWindow(width = 840.dp, height = 479.dp),
        )
    }

    @Test
    fun width719AndHeight600RemainsCompact() {
        assertEquals(
            OneUiLayoutMode.Compact,
            OneUiLayoutMode.fromWindow(width = 719.dp, height = 600.dp),
        )
    }

    @Test
    fun desktopMetricsUseTheDocumentedLibraryDefaults() {
        val metrics = OneUiDesktopMetrics()

        assertEquals(292.dp, metrics.navigationPaneWidth)
        assertEquals(12.dp, metrics.workspaceGutter)
        assertEquals(58.dp, metrics.topBarHeight)
        assertEquals(12.dp, metrics.topBarHorizontalPadding)
        assertEquals(18.dp, metrics.contentRadius)
        assertEquals(48.dp, metrics.navigationRowMinHeight)
        assertEquals(14.dp, metrics.navigationRowHorizontalPadding)
    }
}
