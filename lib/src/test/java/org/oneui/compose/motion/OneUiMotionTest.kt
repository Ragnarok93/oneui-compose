package org.oneui.compose.motion

import org.junit.Assert.assertEquals
import org.junit.Test

class OneUiMotionTest {
    @Test
    fun referenceDurationsMatchSesl8() {
        assertEquals(400, OneUiMotion.Duration.Fab)
        assertEquals(300, OneUiMotion.Duration.SheetEnterTranslation)
        assertEquals(165, OneUiMotion.Duration.SheetEnterAlpha)
        assertEquals(200, OneUiMotion.Duration.SheetExitTranslation)
        assertEquals(140, OneUiMotion.Duration.SheetExitAlpha)
        assertEquals(300, OneUiMotion.Duration.MenuTranslation)
        assertEquals(150, OneUiMotion.Duration.MenuAlpha)
        assertEquals(290, OneUiMotion.Duration.SelectionCheck)
        assertEquals(199, OneUiMotion.Duration.SelectionUncheck)
        assertEquals(300, OneUiMotion.Duration.AppBarCollapse)
        assertEquals(300, OneUiMotion.Duration.Drawer)
        assertEquals(250, OneUiMotion.Duration.NavigationIndicator)
        assertEquals(200, OneUiMotion.Duration.SearchMode)
        assertEquals(200, OneUiMotion.Duration.ActionMode)
        assertEquals(220, OneUiMotion.Duration.PickerWheel)
    }
}
