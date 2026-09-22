package org.oneui.compose.components.selection

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class OneUiCompoundButtonDefaultsTest {
    @Test
    fun labeledCompoundControlsUseThePinnedSesl8TouchTargetAndMargins() {
        assertEquals(48.dp, OneUiCompoundButtonDefaults.MinHeight)
        assertEquals(6.dp, OneUiCompoundButtonDefaults.VerticalMargin)
    }
}
