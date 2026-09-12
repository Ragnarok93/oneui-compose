package org.oneui.compose.motion

import androidx.compose.animation.core.TweenSpec
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class OneUiMotionParityTest {
    @Test
    fun selectionCheckStagesMatchPinnedReference() {
        val first = OneUiMotion.selectionCheckFirstStroke<Float>() as TweenSpec<Float>
        val second = OneUiMotion.selectionCheckSecondStroke<Float>() as TweenSpec<Float>

        assertEquals(110, first.durationMillis)
        assertEquals(1, first.delay)
        assertSame(OneUiEasing.Linear, first.easing)

        assertEquals(180, second.durationMillis)
        assertEquals(110, second.delay)
        assertSame(OneUiEasing.Decelerate, second.easing)
    }

    @Test
    fun selectionUncheckStagesMatchPinnedReference() {
        val first = OneUiMotion.selectionUncheckFirstStroke<Float>() as TweenSpec<Float>
        val second = OneUiMotion.selectionUncheckSecondStroke<Float>() as TweenSpec<Float>

        assertEquals(120, first.durationMillis)
        assertEquals(1, first.delay)
        assertSame(OneUiEasing.Linear, first.easing)

        assertEquals(59, second.durationMillis)
        assertEquals(140, second.delay)
        assertSame(OneUiEasing.Accelerate, second.easing)
    }
}
