package org.oneui.compose.picker.scroll

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import org.oneui.compose.picker.PickerVisuals


@Composable
internal fun ItemScrollOverlay(
    modifier: Modifier = Modifier,
    color: Color,
    edgeAlpha: Float,
    windowHeight: Dp,
) {
    val topStops = PickerVisuals.maskStops(edgeAlpha).map { stop ->
        stop.position to color.copy(alpha = stop.alpha)
    }
    val bottomStops = topStops
        .map { (position, stopColor) -> 1f - position to stopColor }
        .sortedBy { it.first }
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .background(
                    Brush.verticalGradient(*topStops.toTypedArray()),
                )
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(windowHeight)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .background(
                    Brush.verticalGradient(*bottomStops.toTypedArray()),
                )
        )
    }
}
