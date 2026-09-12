package org.oneui.compose.icons

import androidx.compose.runtime.Immutable
import dev.oneuiproject.oneui.R as OneUiIconResources

/** How a drawable exposed by the One UI reference is represented by the Compose library. */
enum class OneUiDrawableKind {
    Icon,
    StatefulAnimated,
    Shape,
    Painter,
    ComposePattern,
}

/** Provenance for a drawable mapping. */
enum class OneUiDrawableSource {
    /** `io.github.oneuiproject:icons`, already consumed by [OneUiIcons]. */
    IconsDependency,

    /** MIT-licensed drawable/state source in the pinned `oneui-design` reference. */
    OneUiDesignReference,

    /** A semantic Compose primitive used instead of shipping a drawable background/state list. */
    ComposePrimitive,
}

/** Stable rendering contract used by the catalog demo and parity tooling. */
@Immutable
sealed interface OneUiDrawableRendering {
    @Immutable
    data class Icon(val icon: OneUiIcon) : OneUiDrawableRendering

    /** Stateful/animated or higher-order Compose implementation identified by its public API. */
    @Immutable
    data class ComposeState(val api: String) : OneUiDrawableRendering

    /** Shape/brush/background semantics represented without a drawable resource. */
    @Immutable
    data class Primitive(val api: String) : OneUiDrawableRendering

    /** Resource-backed painter retained when a raster/vector resource is the faithful contract. */
    @Immutable
    data class Painter(val resourceName: String) : OneUiDrawableRendering
}

/** One source-traceable entry in the One UI drawable catalogue. */
@Immutable
data class OneUiDrawableEntry(
    val name: String,
    val kind: OneUiDrawableKind,
    val source: OneUiDrawableSource,
    val rendering: OneUiDrawableRendering,
    val reference: String? = null,
)

/**
 * Searchable Compose mapping of the drawable surface exercised by the reference sample.
 *
 * The reference sample's `IconsRepo` reflects every field in `dev.oneuiproject.oneui.R.drawable`.
 * This catalogue intentionally does the same for `io.github.oneuiproject:icons:1.1.0`, preserving
 * the exact 883-resource dependency namespace without maintaining a second hand-curated icon list.
 * Reference-only drawable/state resources are then appended as source-traceable Compose mappings.
 */
object OneUiDrawableCatalog {
    private const val IconsReference = "io.github.oneuiproject:icons:1.1.0"
    private const val DesignReference =
        "Ragnarok93/oneui-design@ce4f2cae8c0d712acd2f0b2926ee909fd5d8a434"

    private val dependencyEntries: List<OneUiDrawableEntry> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        OneUiIconResources.drawable::class.java.declaredFields
            .asSequence()
            .filter { field -> field.type == Int::class.javaPrimitiveType }
            .map { field ->
                val resourceId = field.getInt(null)
                OneUiDrawableEntry(
                    name = field.name,
                    kind = OneUiDrawableKind.Icon,
                    source = OneUiDrawableSource.IconsDependency,
                    rendering = OneUiDrawableRendering.Icon(OneUiIcon.Resource(resourceId)),
                    reference = IconsReference,
                )
            }
            .sortedBy(OneUiDrawableEntry::name)
            .toList()
    }

    private val designEntries: List<OneUiDrawableEntry> = listOf(
        referenceState(
            name = "oui_des_list_item_selection_anim_selector",
            api = "OneUiAnimatedIcons.CheckMorph",
            reference = "lib/src/main/res/drawable/oui_des_list_item_selection_anim_selector.xml",
        ),
        referenceState(
            name = "oui_des_list_item_selected_avd",
            api = "OneUiAnimatedIcons.CheckMorph",
            reference = "lib/src/main/res/drawable/oui_des_list_item_selected_avd.xml",
        ),
        referenceState(
            name = "oui_des_list_item_unselected_avd",
            api = "OneUiAnimatedIcons.CheckMorph",
            reference = "lib/src/main/res/drawable/oui_des_list_item_unselected_avd.xml",
        ),
        referencePrimitive(
            name = "oui_des_bottomsheet_drag_handle",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiBottomSheet drag handle",
        ),
        referencePrimitive(
            name = "oui_des_btn_outline_bg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiOutlinedButton",
        ),
        referencePrimitive(
            name = "oui_des_btn_transparent_bg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiTextButton",
        ),
        referencePrimitive(
            name = "oui_des_drawer_menu_item_bg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiAppShell drawer destination",
        ),
        referencePrimitive(
            name = "oui_des_floating_action_bar_layout_bg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiFloatingActionBar",
        ),
        referencePrimitive(
            name = "oui_des_floating_action_bar_selected_btn_bg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiFloatingActionBar selected surface",
        ),
        referencePrimitive(
            name = "oui_des_floating_action_bar_unselected_btn_bg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiFloatingActionBar unselected surface",
        ),
        referencePrimitive(
            name = "oui_des_preference_color_picker_preview",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiColorPreference preview",
        ),
        referencePrimitive(
            name = "oui_des_preference_horizontal_radio_divider_vertical",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiHorizontalRadioPreference divider",
        ),
        referencePrimitive(
            name = "oui_des_preference_seekbar_pro_btn_bg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiSeekBarPreference adjustment button",
        ),
        referencePrimitive(
            name = "oui_des_qr_code_anchor",
            kind = OneUiDrawableKind.ComposePattern,
            api = "OneUiQrCode anchor",
        ),
        referencePrimitive(
            name = "oui_des_qr_clip_scanning_rect",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiQrCode scanning frame",
        ),
    )

    /** Every dependency drawable plus each source-audited design-only mapping implemented so far. */
    val entries: List<OneUiDrawableEntry> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        (dependencyEntries + designEntries)
            .distinctBy(OneUiDrawableEntry::name)
            .sortedBy(OneUiDrawableEntry::name)
    }

    /** Token-based search matching all whitespace-separated terms, like the reference icon list. */
    fun search(query: String): List<OneUiDrawableEntry> {
        val tokens = query
            .trim()
            .split(Regex("\\s+"))
            .filter(String::isNotBlank)
        if (tokens.isEmpty()) return entries
        return entries.filter { entry ->
            tokens.all { token -> entry.name.contains(token, ignoreCase = true) }
        }
    }

    private fun referenceState(
        name: String,
        api: String,
        reference: String,
    ) = OneUiDrawableEntry(
        name = name,
        kind = OneUiDrawableKind.StatefulAnimated,
        source = OneUiDrawableSource.OneUiDesignReference,
        rendering = OneUiDrawableRendering.ComposeState(api),
        reference = reference,
    )

    private fun referencePrimitive(
        name: String,
        kind: OneUiDrawableKind,
        api: String,
    ) = OneUiDrawableEntry(
        name = name,
        kind = kind,
        source = OneUiDrawableSource.OneUiDesignReference,
        rendering = OneUiDrawableRendering.Primitive(api),
        reference = DesignReference,
    )
}
