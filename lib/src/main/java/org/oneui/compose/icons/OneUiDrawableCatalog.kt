package org.oneui.compose.icons

import androidx.compose.runtime.Immutable

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
 * The dependency-backed icon entries below are the stable public icon surface already exposed by
 * [OneUiIcons]. Reference-only drawable/state resources are mapped to the Compose API that owns
 * their behavior instead of copying XML backgrounds into the library. The exhaustive source audit
 * lives outside this Kotlin list and verifies that every reference name has an explicit mapping.
 */
object OneUiDrawableCatalog {
    val entries: List<OneUiDrawableEntry> = listOf(
        icon("ic_oui_add", OneUiIcons.Add),
        icon("ic_oui_remove", OneUiIcons.Remove),
        icon("ic_oui_search", OneUiIcons.Search),
        icon("ic_oui_settings", OneUiIcons.Settings),
        icon("ic_oui_more", OneUiIcons.More),
        icon("ic_oui_back", OneUiIcons.Back),
        icon("ic_oui_arrow_left", OneUiIcons.ArrowLeft),
        icon("ic_oui_arrow_right", OneUiIcons.ArrowRight),
        icon("ic_oui_arrow_up", OneUiIcons.ArrowUp),
        icon("ic_oui_arrow_down", OneUiIcons.ArrowDown),
        icon("ic_oui_keyboard_arrow_left", OneUiIcons.ChevronLeft),
        icon("ic_oui_keyboard_arrow_right", OneUiIcons.ChevronRight),
        icon("ic_oui_keyboard_arrow_up", OneUiIcons.ChevronUp),
        icon("ic_oui_keyboard_arrow_down", OneUiIcons.ChevronDown),
        icon("ic_oui_selected", OneUiIcons.Check),
        icon("ic_oui_checkbox_checked", OneUiIcons.CheckboxChecked),
        icon("ic_oui_checkbox_unchecked", OneUiIcons.CheckboxUnchecked),
        icon("ic_oui_close", OneUiIcons.Close),
        icon("ic_oui_copy", OneUiIcons.Copy),
        icon("ic_oui_delete", OneUiIcons.Delete),
        icon("ic_oui_share", OneUiIcons.Share),
        icon("ic_oui_info", OneUiIcons.Info),
        icon("ic_oui_error", OneUiIcons.Error),
        icon("ic_oui_control_play", OneUiIcons.Play),
        icon("ic_oui_control_pause", OneUiIcons.Pause),
        icon("ic_oui_list_grid", OneUiIcons.Grid),
        icon("ic_oui_home", OneUiIcons.Home),
        icon("ic_oui_motion", OneUiIcons.Motion),
        icon("ic_oui_sysbar_back", OneUiIcons.NavigationBack),
        icon("ic_oui_sysbar_home", OneUiIcons.NavigationHome),
        icon("ic_oui_sysbar_recent", OneUiIcons.NavigationRecents),

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
    ).sortedBy(OneUiDrawableEntry::name)

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

    private fun icon(name: String, icon: OneUiIcon) = OneUiDrawableEntry(
        name = name,
        kind = OneUiDrawableKind.Icon,
        source = OneUiDrawableSource.IconsDependency,
        rendering = OneUiDrawableRendering.Icon(icon),
        reference = "io.github.oneuiproject:icons:1.1.0",
    )

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
        reference = "Ragnarok93/oneui-design@ce4f2cae8c0d712acd2f0b2926ee909fd5d8a434",
    )
}
