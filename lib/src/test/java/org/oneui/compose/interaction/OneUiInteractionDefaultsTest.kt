package org.oneui.compose.interaction

import org.junit.Assert.assertSame
import org.junit.Test
import org.oneui.compose.theme.OneUiShapes

class OneUiInteractionDefaultsTest {
    @Test
    fun defaultPressGeometryUsesTheThemeControlShape() {
        val shapes = OneUiShapes()

        assertSame(shapes.control, OneUiInteractionDefaults.shape(shapes))
    }
}
