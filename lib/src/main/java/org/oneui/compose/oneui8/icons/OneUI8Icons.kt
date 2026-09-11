package org.oneui.compose.oneui8.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

/**
 * Small original vector set built for this Compose fork on a 24x24 optical grid.
 * These are One UI-inspired assets, not extracted Samsung/Figma artwork.
 */
object OneUI8Icons {
    val Search by lazy { stroked("Search", "M10.8,4.2 A6.6,6.6 0,1 0,10.8,17.4 A6.6,6.6 0,1 0,10.8,4.2 M15.6,15.6 L20.1,20.1") }
    val Settings by lazy { stroked("Settings", "M12,8.25 A3.75,3.75 0,1 0,12,15.75 A3.75,3.75 0,1 0,12,8.25 M12,3.4 L13.1,5.25 L15.25,5.85 L17.05,4.75 L19.25,6.95 L18.15,8.75 L18.75,10.9 L20.6,12 L18.75,13.1 L18.15,15.25 L19.25,17.05 L17.05,19.25 L15.25,18.15 L13.1,18.75 L12,20.6 L10.9,18.75 L8.75,18.15 L6.95,19.25 L4.75,17.05 L5.85,15.25 L5.25,13.1 L3.4,12 L5.25,10.9 L5.85,8.75 L4.75,6.95 L6.95,4.75 L8.75,5.85 L10.9,5.25 Z") }
    val MoreVertical by lazy { filled("MoreVertical", "M12,5.2 A1.55,1.55 0,1 0,12,8.3 A1.55,1.55 0,1 0,12,5.2 M12,10.45 A1.55,1.55 0,1 0,12,13.55 A1.55,1.55 0,1 0,12,10.45 M12,15.7 A1.55,1.55 0,1 0,12,18.8 A1.55,1.55 0,1 0,12,15.7") }
    val ChevronRight by lazy { stroked("ChevronRight", "M9,5.8 L15.2,12 L9,18.2") }
    val Check by lazy { stroked("Check", "M5.2,12.4 L9.7,16.7 L18.9,7.5") }
    val Add by lazy { stroked("Add", "M12,5 L12,19 M5,12 L19,12") }
    val Grid by lazy { stroked("Grid", "M5.2,5.2 L10.1,5.2 L10.1,10.1 L5.2,10.1 Z M13.9,5.2 L18.8,5.2 L18.8,10.1 L13.9,10.1 Z M5.2,13.9 L10.1,13.9 L10.1,18.8 L5.2,18.8 Z M13.9,13.9 L18.8,13.9 L18.8,18.8 L13.9,18.8 Z") }
    val Motion by lazy { stroked("Motion", "M4.5,14.5 C6.8,8.2 9.3,8.1 11.1,12 C12.7,15.6 14.6,15.8 19.5,8.6 M4.5,18.1 C8.7,13.1 11.2,13.4 13.2,16 C14.7,18 16.4,18.2 19.5,14.9") }

    private fun stroked(name: String, pathData: String): ImageVector = ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).addPath(
        pathData = PathParser().parsePathString(pathData).toNodes(),
        fill = null,
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.8f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
    ).build()

    private fun filled(name: String, pathData: String): ImageVector = ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).addPath(
        pathData = PathParser().parsePathString(pathData).toNodes(),
        fill = SolidColor(Color.Black),
    ).build()
}
