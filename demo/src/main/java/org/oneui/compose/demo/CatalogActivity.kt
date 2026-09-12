package org.oneui.compose.demo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.oneui.compose.theme.OneUiTheme

class CatalogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OneUiTheme {
                CatalogApp(
                    onOpenLegacyShowcase = {
                        startActivity(Intent(this@CatalogActivity, MainActivity::class.java))
                    },
                )
            }
        }
    }
}
