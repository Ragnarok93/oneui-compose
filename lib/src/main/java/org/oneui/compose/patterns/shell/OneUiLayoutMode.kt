package org.oneui.compose.patterns.shell

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class OneUiLayoutMode {
    Compact,
    Desktop;

    companion object {
        fun fromWindow(width: Dp, height: Dp): OneUiLayoutMode =
            if (width >= 840.dp && height >= 480.dp) Desktop else Compact
    }
}
