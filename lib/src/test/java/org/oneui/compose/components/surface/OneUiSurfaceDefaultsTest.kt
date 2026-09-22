package org.oneui.compose.components.surface

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.oneui.compose.theme.oneUiColorsFromLegacy
import org.oneui.compose.theme.color.DarkColorTheme

class OneUiSurfaceDefaultsTest {
    @Test
    fun genericCardsUseTheOpaqueSeslFloatingSurfaceNotTheBluePreferenceOverlay() {
        val colors = oneUiColorsFromLegacy(DarkColorTheme, dark = true)

        val container = OneUiSurfaceDefaults.containerColor(colors)

        assertEquals(Color(0xff252525), container)
        assertNotEquals(colors.surface, container)
    }
}
