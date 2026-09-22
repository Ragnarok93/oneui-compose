package org.oneui.compose.demo.screens

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class PickerReferenceDefaultsTest {
    @Test
    fun dialogActionsKeepThePinnedSesl8Width() {
        assertEquals(250.dp, PickerReferenceDefaults.DialogButtonWidth)
    }
}
