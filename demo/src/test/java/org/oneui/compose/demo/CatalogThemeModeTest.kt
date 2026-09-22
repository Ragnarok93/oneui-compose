package org.oneui.compose.demo

import org.junit.Assert.assertEquals
import org.junit.Test

class CatalogThemeModeTest {
    @Test
    fun appearanceChoicesResolveToAConcreteThemeOverrideOnlyWhenRequested() {
        assertEquals(null, CatalogThemeMode.System.darkThemeOverride)
        assertEquals(false, CatalogThemeMode.Light.darkThemeOverride)
        assertEquals(true, CatalogThemeMode.Dark.darkThemeOverride)
    }
}
