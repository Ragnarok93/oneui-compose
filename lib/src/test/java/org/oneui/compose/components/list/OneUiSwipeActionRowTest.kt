package org.oneui.compose.components.list

import org.junit.Assert.assertEquals
import org.junit.Test

class OneUiSwipeActionRowTest {
    @Test
    fun positivePhysicalOffsetMapsToRightAction() {
        assertEquals(
            OneUiSwipeDirection.Right,
            oneUiSwipeDirection(offsetPx = 72f, thresholdPx = 48f),
        )
    }

    @Test
    fun negativePhysicalOffsetMapsToLeftAction() {
        assertEquals(
            OneUiSwipeDirection.Left,
            oneUiSwipeDirection(offsetPx = -72f, thresholdPx = 48f),
        )
    }

    @Test
    fun belowThresholdDoesNotCommitAction() {
        assertEquals(
            OneUiSwipeDirection.None,
            oneUiSwipeDirection(offsetPx = 47.9f, thresholdPx = 48f),
        )
        assertEquals(
            OneUiSwipeDirection.None,
            oneUiSwipeDirection(offsetPx = -47.9f, thresholdPx = 48f),
        )
    }

    @Test
    fun invalidThresholdNeverCommits() {
        assertEquals(OneUiSwipeDirection.None, oneUiSwipeDirection(100f, 0f))
        assertEquals(OneUiSwipeDirection.None, oneUiSwipeDirection(Float.NaN, 48f))
    }
}
