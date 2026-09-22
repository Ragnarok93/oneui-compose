package org.oneui.compose.demo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.components.qrcode.OneUiQrCode
import org.oneui.compose.components.qrcode.OneUiQrErrorCorrection
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.theme.OneUiTheme

@Composable
fun QrCodesScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("catalog-qr-codes"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            QrSampleCard(
                title = "Rounded QR with icon",
                subtitle = "High error correction with a centered One UI glyph",
                testTag = "qr-icon-rounded",
            ) {
                Box(
                    modifier = Modifier
                        .size(248.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color.White)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    OneUiQrCode(
                        data = "https://github.com/Ragnarok93/oneui-compose",
                        modifier = Modifier.fillMaxSize(),
                        foregroundColor = Color.Black,
                        backgroundColor = Color.White,
                        errorCorrection = OneUiQrErrorCorrection.High,
                        contentDescription = "Rounded QR code with One UI icon",
                    )
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center,
                    ) {
                        OneUiIcon(
                            icon = OneUiIcons.Home,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Black,
                        )
                    }
                }
            }
        }

        item {
            QrSampleCard(
                title = "Square QR",
                subtitle = "Standard square presentation without a center icon",
                testTag = "qr-square-no-icon",
            ) {
                OneUiQrCode(
                    data = "oneui-compose:sample:plain",
                    modifier = Modifier.size(232.dp),
                    foregroundColor = Color.Black,
                    backgroundColor = Color.White,
                    contentDescription = "Square QR code",
                )
            }
        }

        item {
            QrSampleCard(
                title = "Custom tint",
                subtitle = "Theme-aware QR rendering with custom foreground and surface colors",
                testTag = "qr-custom-tint",
            ) {
                OneUiQrCode(
                    data = "oneui-compose:sample:tinted",
                    modifier = Modifier
                        .size(232.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    foregroundColor = OneUiTheme.colors.accent,
                    backgroundColor = OneUiTheme.colors.surfaceElevated,
                    errorCorrection = OneUiQrErrorCorrection.Quartile,
                    contentDescription = "Tinted QR code",
                )
            }
        }

        item {
            QrSampleCard(
                title = "Empty/default state",
                subtitle = "Empty data keeps the QR surface visible without generating modules",
                testTag = "qr-empty-default",
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    OneUiQrCode(
                        data = "",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(24.dp)),
                        foregroundColor = OneUiTheme.colors.primaryText,
                        backgroundColor = OneUiTheme.colors.surfaceElevated,
                        contentDescription = "Empty QR code state",
                    )
                }
            }
        }
    }
}

@Composable
private fun QrSampleCard(
    title: String,
    subtitle: String,
    testTag: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clip(RoundedCornerShape(28.dp))
            .background(OneUiTheme.colors.surfaceElevated)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                color = OneUiTheme.colors.primaryText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = subtitle,
                color = OneUiTheme.colors.secondaryText,
                fontSize = 13.sp,
            )
        }
        content()
    }
}
