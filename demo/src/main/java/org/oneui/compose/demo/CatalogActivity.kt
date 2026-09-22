package org.oneui.compose.demo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import org.oneui.compose.theme.OneUiTheme

class CatalogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var themeMode by rememberSaveable { mutableStateOf(CatalogThemeMode.System) }
            val darkTheme = themeMode.darkThemeOverride ?: isSystemInDarkTheme()
            val view = LocalView.current
            SideEffect {
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }
            OneUiTheme(darkTheme = darkTheme) {
                CatalogApp(
                    themeMode = themeMode,
                    onThemeModeChange = { themeMode = it },
                    onOpenLegacyShowcase = {
                        startActivity(Intent(this@CatalogActivity, MainActivity::class.java))
                    },
                )
            }
        }
    }
}
