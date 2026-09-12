package org.oneui.compose.components.progress

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import org.oneui.compose.progress.CircularProgressIndicatorSize
import org.oneui.compose.progress.ProgressIndicator
import org.oneui.compose.progress.ProgressIndicatorColors
import org.oneui.compose.progress.ProgressIndicatorType
import org.oneui.compose.theme.OneUiTheme

/** Stable size choices backed by the existing One UI circular progress geometry. */
enum class OneUiCircularProgressSize {
    Small,
    Medium,
    Large,
    XLarge,
}

@Immutable
data class OneUiProgressColors(
    val track: Color,
    val progress: Color,
    val secondaryProgress: Color,
)

object OneUiProgressDefaults {
    @Composable
    fun colors(
        track: Color = OneUiTheme.colors.controlInactive.copy(alpha = 0.24f),
        progress: Color = OneUiTheme.colors.accent,
        secondaryProgress: Color = OneUiTheme.colors.accent.copy(alpha = 0.55f),
    ): OneUiProgressColors = OneUiProgressColors(
        track = track,
        progress = progress,
        secondaryProgress = secondaryProgress,
    )
}

/** One UI linear progress indicator. Pass `null` for indeterminate progress. */
@Composable
fun OneUiLinearProgress(
    progress: Float?,
    modifier: Modifier = Modifier,
    colors: OneUiProgressColors = OneUiProgressDefaults.colors(),
) {
    val coerced = progress?.takeIf(Float::isFinite)?.coerceIn(0f, 1f)
    val semanticsModifier = Modifier.semantics {
        progressBarRangeInfo = if (coerced == null) {
            ProgressBarRangeInfo.Indeterminate
        } else {
            ProgressBarRangeInfo(coerced, 0f..1f)
        }
    }
    ProgressIndicator(
        modifier = modifier.then(semanticsModifier),
        type = if (coerced == null) {
            ProgressIndicatorType.HorizontalIndeterminate
        } else {
            ProgressIndicatorType.HorizontalDeterminate(coerced)
        },
        colors = colors.asLegacy(),
    )
}

/** One UI circular progress indicator. Pass `null` for indeterminate progress. */
@Composable
fun OneUiCircularProgress(
    progress: Float?,
    modifier: Modifier = Modifier,
    size: OneUiCircularProgressSize = OneUiCircularProgressSize.Medium,
    colors: OneUiProgressColors = OneUiProgressDefaults.colors(),
) {
    val coerced = progress?.takeIf(Float::isFinite)?.coerceIn(0f, 1f)
    Box(
        modifier = modifier.semantics {
            progressBarRangeInfo = if (coerced == null) {
                ProgressBarRangeInfo.Indeterminate
            } else {
                ProgressBarRangeInfo(coerced, 0f..1f)
            }
        },
    ) {
        ProgressIndicator(
            type = if (coerced == null) {
                ProgressIndicatorType.CircularIndeterminate(size.asLegacy())
            } else {
                ProgressIndicatorType.CircularDeterminate(size.asLegacy(), coerced)
            },
            colors = colors.asLegacy(),
        )
    }
}

private fun OneUiProgressColors.asLegacy(): ProgressIndicatorColors = ProgressIndicatorColors(
    neutral = track,
    progress = progress,
    secondaryProgress = secondaryProgress,
)

private fun OneUiCircularProgressSize.asLegacy(): CircularProgressIndicatorSize = when (this) {
    OneUiCircularProgressSize.Small -> CircularProgressIndicatorSize.Companion.Small
    OneUiCircularProgressSize.Medium -> CircularProgressIndicatorSize.Companion.Medium
    OneUiCircularProgressSize.Large -> CircularProgressIndicatorSize.Companion.Large
    OneUiCircularProgressSize.XLarge -> CircularProgressIndicatorSize.Companion.XLarge
}
