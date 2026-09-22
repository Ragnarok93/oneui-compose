package org.oneui.compose.components.qr

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.oneui.compose.components.qrcode.OneUiQrCode as StableOneUiQrCode
import org.oneui.compose.components.qrcode.OneUiQrErrorCorrection

/** Package-forwarding QR API for the modern component namespace. */
@Composable
fun OneUiQrCode(
    data: String,
    modifier: Modifier = Modifier,
    foregroundColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    errorCorrection: OneUiQrErrorCorrection = OneUiQrErrorCorrection.Medium,
    quietZoneModules: Int = 4,
    contentDescription: String? = "QR code",
) = StableOneUiQrCode(
    data = data,
    modifier = modifier,
    foregroundColor = foregroundColor,
    backgroundColor = backgroundColor,
    errorCorrection = errorCorrection,
    quietZoneModules = quietZoneModules,
    contentDescription = contentDescription ?: "QR code",
)
