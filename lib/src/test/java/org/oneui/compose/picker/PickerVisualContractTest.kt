package org.oneui.compose.picker

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PickerVisualContractTest {
    @Test
    fun wheelTextUsesReadableOneUiForegroundRoles() {
        val visuals = PickerVisuals.from(
            primaryText = Color(0xfffafafa),
            secondaryText = Color(0xff999999),
            surface = Color(0xff252525),
        )

        assertEquals(Color(0xfffafafa), visuals.activeText)
        assertEquals(Color(0xff999999), visuals.inactiveText)
        assertEquals(Color(0xff252525), visuals.surface)
    }

    @Test
    fun wheelMaskFadesIntoTheSelectionWindowInsteadOfDrawingAHardRectangle() {
        val stops = PickerVisuals.maskStops(edgeAlpha = PickerVisuals.IdleMaskEdgeAlpha)

        assertEquals(0f, stops.first().position, 0.0001f)
        assertEquals(PickerVisuals.IdleMaskEdgeAlpha, stops.first().alpha, 0.0001f)
        assertEquals(1f, stops.last().position, 0.0001f)
        assertEquals(0f, stops.last().alpha, 0.0001f)
        assertTrue(stops.zipWithNext().all { (before, after) -> before.alpha >= after.alpha })
    }
}
