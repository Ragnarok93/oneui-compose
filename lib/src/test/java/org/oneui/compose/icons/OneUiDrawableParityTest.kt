package org.oneui.compose.icons

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OneUiDrawableParityTest {
    @Test
    fun catalogNamesAreUniqueAndStable() {
        val names = OneUiDrawableCatalog.entries.map { it.name }
        assertEquals(names.toSet().size, names.size)
        assertTrue(names.contains("ic_oui_add"))
        assertTrue(names.contains("ic_oui_search"))
        assertTrue(names.contains("oui_des_list_item_selection_anim_selector"))
    }

    @Test
    fun searchMatchesAllWhitespaceSeparatedTokensCaseInsensitively() {
        val result = OneUiDrawableCatalog.search("LIST selection")
        assertTrue(result.isNotEmpty())
        assertTrue(result.all { entry ->
            entry.name.contains("list", ignoreCase = true) &&
                entry.name.contains("selection", ignoreCase = true)
        })
    }

    @Test
    fun catalogCarriesRenderingAndProvenanceMetadata() {
        val add = OneUiDrawableCatalog.entries.single { it.name == "ic_oui_add" }
        assertEquals(OneUiDrawableKind.Icon, add.kind)
        assertEquals(OneUiDrawableSource.IconsDependency, add.source)
        assertTrue(add.rendering is OneUiDrawableRendering.Icon)

        val selection = OneUiDrawableCatalog.entries.single {
            it.name == "oui_des_list_item_selection_anim_selector"
        }
        assertEquals(OneUiDrawableKind.StatefulAnimated, selection.kind)
        assertEquals(OneUiDrawableSource.OneUiDesignReference, selection.source)
        assertEquals(
            "OneUiAnimatedIcons.CheckMorph",
            (selection.rendering as OneUiDrawableRendering.ComposeState).api,
        )
    }
}
