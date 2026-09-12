package org.oneui.compose.components.qrcode

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import org.oneui.compose.theme.OneUiTheme

/** Error-correction strength for [OneUiQrCode] without exposing encoder-specific types. */
enum class OneUiQrErrorCorrection {
    Low,
    Medium,
    Quartile,
    High,
}

/**
 * Compose-native, scannable QR-code surface.
 *
 * Encoding is delegated to the Apache-2.0 ZXing core library; rendering remains Compose Canvas
 * code and no Android View is retained by the component. The generated matrix is remembered by
 * input so drawing does not repeat QR encoding during animation or unrelated recomposition.
 */
@Composable
fun OneUiQrCode(
    data: String,
    modifier: Modifier = Modifier,
    foregroundColor: Color = OneUiTheme.colors.primaryText,
    backgroundColor: Color = OneUiTheme.colors.surface,
    errorCorrection: OneUiQrErrorCorrection = OneUiQrErrorCorrection.Medium,
    quietZoneModules: Int = 4,
    contentDescription: String = "QR code",
) {
    require(quietZoneModules >= 0) { "quietZoneModules must be non-negative" }

    val matrix = remember(data, errorCorrection, quietZoneModules) {
        if (data.isEmpty()) null else encodeMatrix(data, errorCorrection, quietZoneModules)
    }

    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .semantics { this.contentDescription = contentDescription },
    ) {
        drawRect(backgroundColor)
        val qr = matrix ?: return@Canvas
        val moduleSize = minOf(size.width / qr.size, size.height / qr.size)
        val renderedSize = moduleSize * qr.size
        val origin = Offset(
            x = (size.width - renderedSize) / 2f,
            y = (size.height - renderedSize) / 2f,
        )

        for (y in 0 until qr.size) {
            for (x in 0 until qr.size) {
                if (!qr[x, y]) continue
                drawRect(
                    color = foregroundColor,
                    topLeft = Offset(
                        x = origin.x + x * moduleSize,
                        y = origin.y + y * moduleSize,
                    ),
                    size = Size(moduleSize + 0.25f, moduleSize + 0.25f),
                )
            }
        }
    }
}

@Immutable
private class QrMatrix(
    val size: Int,
    private val cells: BooleanArray,
) {
    operator fun get(x: Int, y: Int): Boolean = cells[y * size + x]
}

private fun encodeMatrix(
    data: String,
    errorCorrection: OneUiQrErrorCorrection,
    quietZoneModules: Int,
): QrMatrix {
    val level = when (errorCorrection) {
        OneUiQrErrorCorrection.Low -> ErrorCorrectionLevel.L
        OneUiQrErrorCorrection.Medium -> ErrorCorrectionLevel.M
        OneUiQrErrorCorrection.Quartile -> ErrorCorrectionLevel.Q
        OneUiQrErrorCorrection.High -> ErrorCorrectionLevel.H
    }
    val matrix = QRCodeWriter().encode(
        data,
        BarcodeFormat.QR_CODE,
        1,
        1,
        mapOf(
            EncodeHintType.ERROR_CORRECTION to level,
            EncodeHintType.MARGIN to quietZoneModules,
        ),
    )
    val side = matrix.width
    val cells = BooleanArray(side * side)
    for (y in 0 until side) {
        for (x in 0 until side) {
            cells[y * side + x] = matrix[x, y]
        }
    }
    return QrMatrix(side, cells)
}
