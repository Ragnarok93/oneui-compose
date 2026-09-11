package org.oneui.compose.oneui8

import org.junit.Assert.assertEquals
import org.junit.Test
import org.oneui.compose.oneui8.components.normalizeOneUI8SliderValue
import org.oneui.compose.oneui8.motion.OneUI8Motion

class OneUI8TokensTest {
    @Test
    fun seslReferenceDurationsRemainStable() {
        assertEquals(110, OneUI8Motion.Duration.Press)
        assertEquals(290, OneUI8Motion.Duration.SelectionTotal)
        assertEquals(165, OneUI8Motion.Duration.SheetEnterAlpha)
        assertEquals(300, OneUI8Motion.Duration.SheetEnterTranslation)
        assertEquals(140, OneUI8Motion.Duration.SheetExitAlpha)
        assertEquals(200, OneUI8Motion.Duration.SheetExitTranslation)
    }

    @Test
    fun sliderValueIsClampedToRange() {
        assertEquals(0f, normalizeOneUI8SliderValue(-1f, 0f..1f), 0f)
        assertEquals(0.4f, normalizeOneUI8SliderValue(0.4f, 0f..1f), 0f)
        assertEquals(1f, normalizeOneUI8SliderValue(2f, 0f..1f), 0f)
    }
}
