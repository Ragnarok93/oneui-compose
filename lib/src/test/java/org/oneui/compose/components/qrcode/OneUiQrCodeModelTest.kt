package org.oneui.compose.components.qrcode

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OneUiQrCodeModelTest {
    @Test
    fun emptyPayloadHasNoMatrix() {
        assertNull(oneUiQrMatrix(""))
    }

    @Test
    fun encoderReturnsReadableSquareMatrix() {
        val matrix = oneUiQrMatrix(
            data = "https://example.com/profile/ada",
            errorCorrection = OneUiQrErrorCorrection.Medium,
            quietZoneModules = 4,
        )

        assertNotNull(matrix)
        matrix!!
        assertTrue(matrix.size > 0)
        assertTrue(
            (0 until matrix.size).any { y ->
                (0 until matrix.size).any { x -> matrix[x, y] }
            },
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun negativeQuietZoneIsRejected() {
        oneUiQrMatrix("data", quietZoneModules = -1)
    }
}
