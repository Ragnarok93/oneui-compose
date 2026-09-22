package org.oneui.compose.theme

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test
import org.oneui.compose.theme.color.DarkColorTheme
import org.oneui.compose.theme.color.LightColorTheme

class OneUiThemeColorContractTest {
    @Test
    fun lightPaletteMapsPinnedSeslAndDesignRoles() {
        val colors = oneUiColorsFromLegacy(LightColorTheme, dark = false)

        assertEquals(Color(0xff3e91ff), colors.accent)
        assertEquals(Color(0xff0381fe), colors.accentStrong)
        assertEquals(Color(0xfffcfcfc), colors.background)
        assertEquals(Color(0x0f0381fe), colors.surface)
        assertEquals(Color(0xff474747), colors.tooltipBackground)
        assertEquals(Color(0xfffafafa), colors.tooltipContent)
        assertEquals(Color(0xffef5e16), colors.seekOverlapActive)
        assertEquals(Color(0xff252525), colors.tabIndicator)
        assertEquals(Color(0xffe4e4e7), colors.tabRoundedBackground)
        assertEquals(Color(0xfffafaff), colors.tabRoundedSelectedText)
        assertEquals(Color(0xff848487), colors.tabRoundedUnselectedText)
        assertEquals(Color(0xff010101), colors.tabSelectedText)
    }

    @Test
    fun darkPaletteKeepsReferenceFunctionalAndNavigationRoles() {
        val colors = oneUiColorsFromLegacy(DarkColorTheme, dark = true)

        assertEquals(Color(0xff171717), colors.background)
        assertEquals(Color(0xff5ad69e), colors.functionalPositive)
        assertEquals(Color(0xffff6021), colors.seekOverlapActive)
        assertEquals(Color(0xff010101), colors.navigationBackground)
        assertEquals(Color(0xfffafafa), colors.tabIndicator)
        assertEquals(Color(0xff47474a), colors.tabRoundedBackground)
        assertEquals(Color(0xffa3a3a7), colors.tabRoundedUnselectedText)
    }
}
