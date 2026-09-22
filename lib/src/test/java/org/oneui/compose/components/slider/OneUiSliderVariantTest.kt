package org.oneui.compose.components.slider

import org.junit.Assert.assertEquals
import org.junit.Test

class OneUiSliderVariantTest {
    @Test
    fun levelBarTickMathUsesTheReferenceInclusiveRange() {
        assertEquals(14f, snapSliderValue(14f, 10f..20f, steps = 9), 0f)
    }

    @Test
    fun plusSteppedMathProducesIntegerStops() {
        assertEquals(1f, snapSliderValue(0.8f, 0f..2f, steps = 1), 0f)
        assertEquals(0.8f, snapSliderValue(0.8f, 0f..2f, steps = 0), 0f)
    }
}
