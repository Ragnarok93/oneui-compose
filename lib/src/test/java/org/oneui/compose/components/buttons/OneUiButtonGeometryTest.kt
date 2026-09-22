package org.oneui.compose.components.buttons

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class OneUiButtonGeometryTest {
    @Test
    fun containedButtonUsesThePinnedSesl8SizeAndCornerRadius() {
        assertEquals(200.dp, OneUiContainedButtonDefaults.MinWidth)
        assertEquals(52.dp, OneUiContainedButtonDefaults.MinHeight)
        assertEquals(26.dp, OneUiContainedButtonDefaults.CornerRadius)
    }

    @Test
    fun outlineButtonUsesThePinnedSesl8MinimumSize() {
        assertEquals(196.dp, OneUiButtonDefaults.OutlinedMinWidth)
        assertEquals(36.dp, OneUiButtonDefaults.OutlinedMinHeight)
    }
}
