package org.oneui.compose.demo.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.oneui.compose.patterns.about.OneUiAboutLink
import org.oneui.compose.patterns.about.OneUiCustomAbout

@Composable
fun CustomAboutScreen(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
) {
    OneUiCustomAbout(
        appName = "OneUI Compose",
        version = "0.8.0-oneui8-demo",
        contributors = listOf("Yanndroid", "Salvo Giangreco", "Tribalfs"),
        licenses = listOf(
            "Apache License 2.0",
            "MIT License",
        ),
        links = listOf(
            OneUiAboutLink("Android Jetpack") { },
            OneUiAboutLink("Material Components") { },
            OneUiAboutLink("SESL Android Jetpack (Unofficial)") { },
            OneUiAboutLink("SESL Material Components (Unofficial)") { },
            OneUiAboutLink("OneUI Design Lib") { },
        ),
        modifier = modifier,
        onBack = onBack,
    )
}

