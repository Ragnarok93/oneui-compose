package org.oneui.compose.components.slider

import org.junit.Assert.assertEquals
import org.junit.Test

class SliderMathTest {
    @Test
    fun stepsSnapAcrossArbitraryRange() {
        assertEquals(15f, snapSliderValue(14f, 10f..20f, steps = 1), 0f)
    }

    @Test
    fun rtlFractionReversesHorizontalAxis() {
        assertEquals(.75f, sliderFraction(25f, 100f, rtl = true), .0001f)
    }

    @Test
    fun visibleTrackEndpointsRemainReachable() {
        assertEquals(0f, sliderTrackFraction(16f, 160f, 16f, rtl = false), 0f)
        assertEquals(1f, sliderTrackFraction(144f, 160f, 16f, rtl = false), 0f)
        assertEquals(.5f, sliderTrackFraction(80f, 160f, 16f, rtl = false), .0001f)
    }

    @Test
    fun visibleTrackEndpointsRespectRtlAndVerticalDirection() {
        assertEquals(1f, sliderTrackFraction(16f, 160f, 16f, rtl = true), 0f)
        assertEquals(0f, sliderTrackFraction(144f, 160f, 16f, rtl = true), 0f)
        assertEquals(1f, verticalSliderTrackFraction(16f, 160f, 16f), 0f)
        assertEquals(0f, verticalSliderTrackFraction(144f, 160f, 16f), 0f)
    }

    @Test
    fun nonFiniteValueReturnsRangeStart() {
        assertEquals(3f, coerceSliderValue(Float.NaN, 3f..9f), 0f)
    }

    @Test
    fun valueFractionRespectsArbitraryRange() {
        assertEquals(.4f, sliderValueFraction(14f, 10f..20f), .0001f)
    }

    @Test
    fun zeroLengthRangeIsStable() {
        assertEquals(4f, snapSliderValue(99f, 4f..4f, steps = 4), 0f)
        assertEquals(0f, sliderValueFraction(4f, 4f..4f), 0f)
    }
}