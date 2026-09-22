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

    /** Remaining drawable names in the pinned reference, represented by the owning Compose API. */
    private val additionalDesignEntries: List<OneUiDrawableEntry> = listOf(
        referencePrimitive(
            name = "oui_des_drawer_menu_category_bg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiAppShell drawer category background",
            reference = "lib/src/main/res/drawable/oui_des_drawer_menu_category_bg.xml",
        ),
        referencePrimitive(
            name = "oui_des_drawer_menu_category_bg_navrail",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiNavigationRail category background",
            reference = "lib/src/main/res/drawable/oui_des_drawer_menu_category_bg_navrail.xml",
        ),
        referencePrimitive(
            name = "oui_des_drawer_menu_category_fg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiAppShell drawer category foreground",
            reference = "lib/src/main/res/drawable/oui_des_drawer_menu_category_fg.xml",
        ),
        referencePrimitive(
            name = "oui_des_drawer_menu_dotted_separator",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiAppShell drawer separator",
            reference = "lib/src/main/res/drawable/oui_des_drawer_menu_dotted_separator.xml",
        ),
        referencePrimitive(
            name = "oui_des_drawer_menu_expand_icon",
            kind = OneUiDrawableKind.Icon,
            api = "OneUiIcons.ChevronDown",
            reference = "lib/src/main/res/drawable/oui_des_drawer_menu_expand_icon.xml",
        ),
        referencePrimitive(
            name = "oui_des_floating_action_bar_selected_ripple",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiFloatingActionBar selected ripple",
            reference = "lib/src/main/res/drawable/oui_des_floating_action_bar_selected_ripple.xml",
        ),
        referencePrimitive(
            name = "oui_des_floating_action_bar_unselected_ripple",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiFloatingActionBar unselected ripple",
            reference = "lib/src/main/res/drawable/oui_des_floating_action_bar_unselected_ripple.xml",
        ),
        referencePrimitive(
            name = "oui_des_grid_menu_dialog_item_bg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiMenu grid item background",
            reference = "lib/src/main/res/drawable/oui_des_grid_menu_dialog_item_bg.xml",
        ),
        referencePrimitive(
            name = "oui_des_ic_ab_app_info",
            kind = OneUiDrawableKind.Icon,
            api = "OneUiIcons.Info",
            reference = "lib/src/main/res/drawable/oui_des_ic_ab_app_info.xml",
        ),
        referencePrimitive(
            name = "oui_des_ic_ab_drawer",
            kind = OneUiDrawableKind.Icon,
            api = "OneUiIcons.Grid",
            reference = "lib/src/main/res/drawable/oui_des_ic_ab_drawer.xml",
        ),
        referencePrimitive(
            name = "oui_des_ic_ab_search",
            kind = OneUiDrawableKind.Icon,
            api = "OneUiIcons.Search",
            reference = "lib/src/main/res/drawable/oui_des_ic_ab_search.xml",
        ),
        referenceState(
            name = "oui_des_list_item_selected_icon",
            api = "OneUiAnimatedIcons.CheckMorph",
            reference = "lib/src/main/res/drawable/oui_des_list_item_selected_icon.xml",
        ),
        referenceState(
            name = "oui_des_list_item_unselected_icon",
            api = "OneUiAnimatedIcons.CheckMorph",
            reference = "lib/src/main/res/drawable/oui_des_list_item_unselected_icon.xml",
        ),
        referencePrimitive(
            name = "oui_des_preference_seekbar_pro_btn_bg_mask",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiSeekBarPlus step button mask",
            reference = "lib/src/main/res/drawable/oui_des_preference_seekbar_pro_btn_bg_mask.xml",
        ),
        referencePrimitive(
            name = "oui_des_preference_seekbar_pro_minus",
            kind = OneUiDrawableKind.Icon,
            api = "OneUiSeekBarPlus decrement action",
            reference = "lib/src/main/res/drawable/oui_des_preference_seekbar_pro_minus.xml",
        ),
        referencePrimitive(
            name = "oui_des_preference_seekbar_pro_plus",
            kind = OneUiDrawableKind.Icon,
            api = "OneUiSeekBarPlus increment action",
            reference = "lib/src/main/res/drawable/oui_des_preference_seekbar_pro_plus.xml",
        ),
        referencePrimitive(
            name = "oui_des_preference_suggestion_card_button_bg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiSuggestionCard action button",
            reference = "lib/src/main/res/drawable/oui_des_preference_suggestion_card_button_bg.xml",
        ),
        referencePrimitive(
            name = "oui_des_preference_suggestion_card_close_btn",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiSuggestionCard close button",
            reference = "lib/src/main/res/drawable/oui_des_preference_suggestion_card_close_btn.xml",
        ),
        referencePrimitive(
            name = "oui_des_preference_suggestion_card_icon",
            kind = OneUiDrawableKind.Icon,
            api = "OneUiSuggestionCard icon",
            reference = "lib/src/main/res/drawable/oui_des_preference_suggestion_card_icon.xml",
        ),
        referencePrimitive(
            name = "oui_des_preference_suggestion_close_btn_icon",
            kind = OneUiDrawableKind.Icon,
            api = "OneUiSuggestionCard close icon",
            reference = "lib/src/main/res/drawable/oui_des_preference_suggestion_close_btn_icon.xml",
        ),
        referencePrimitive(
            name = "oui_des_preference_tipscard_button_bg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiTipsCard button",
            reference = "lib/src/main/res/drawable/oui_des_preference_tipscard_button_bg.xml",
        ),
        referencePrimitive(
            name = "oui_des_preference_tipscard_close",
            kind = OneUiDrawableKind.Icon,
            api = "OneUiTipsCard close action",
            reference = "lib/src/main/res/drawable/oui_des_preference_tipscard_close.xml",
        ),
        referencePrimitive(
            name = "oui_des_qr_flash_high_contrast_background",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiQrCode flash control",
            reference = "lib/src/main/res/drawable/oui_des_qr_flash_high_contrast_background.xml",
        ),
        referencePrimitive(
            name = "oui_des_qr_gallery_btn",
            kind = OneUiDrawableKind.Icon,
            api = "OneUiQrCode gallery action",
            reference = "lib/src/main/res/drawable/oui_des_qr_gallery_btn.xml",
        ),
        referencePrimitive(
            name = "oui_des_qr_gallery_high_contrast",
            kind = OneUiDrawableKind.Icon,
            api = "OneUiQrCode gallery action high contrast",
            reference = "lib/src/main/res/drawable/oui_des_qr_gallery_high_contrast.xml",
        ),
        referencePrimitive(
            name = "oui_des_qr_roi",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiQrCode region of interest",
            reference = "lib/src/main/res/drawable/oui_des_qr_roi.9.png",
        ),
        referencePrimitive(
            name = "oui_des_qr_scanning_rect",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiQrCode scanning rectangle",
            reference = "lib/src/main/res/drawable/oui_des_qr_scanning_rect.xml",
        ),
        referencePrimitive(
            name = "oui_des_relative_links_card_bg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiRelatedLinksCard background",
            reference = "lib/src/main/res/drawable/oui_des_relative_links_card_bg.xml",
        ),
        referencePrimitive(
            name = "oui_des_relative_links_item_bg",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiRelatedLinksCard item background",
            reference = "lib/src/main/res/drawable/oui_des_relative_links_item_bg.xml",
        ),
        referencePrimitive(
            name = "oui_des_rounded_tab_background",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiTabs background",
            reference = "lib/src/main/res/drawable/oui_des_rounded_tab_background.xml",
        ),
        referencePrimitive(
            name = "oui_des_rounded_tab_background_selected",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiTabs selected background",
            reference = "lib/src/main/res/drawable/oui_des_rounded_tab_background_selected.xml",
        ),
        referencePrimitive(
            name = "oui_des_rounded_tab_background_unselected",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiTabs unselected background",
            reference = "lib/src/main/res/drawable/oui_des_rounded_tab_background_unselected.xml",
        ),
        referencePrimitive(
            name = "oui_des_rounded_tab_layout_background",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiTabs layout background",
            reference = "lib/src/main/res/drawable/oui_des_rounded_tab_layout_background.xml",
        ),
        referencePrimitive(
            name = "oui_des_seekbar_tick_mark",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiLevelSlider tick marks",
            reference = "lib/src/main/res/drawable/oui_des_seekbar_tick_mark.xml",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_balloon_bg_left",
            kind = OneUiDrawableKind.Painter,
            api = "OneUiTipPopup left balloon",
            reference = "lib/src/main/res/drawable-desk-mdpi/oui_des_tip_popup_balloon_bg_left.9.png",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_balloon_bg_left_translucent",
            kind = OneUiDrawableKind.Painter,
            api = "OneUiTipPopup translucent left balloon",
            reference = "lib/src/main/res/drawable-desk-mdpi/oui_des_tip_popup_balloon_bg_left_translucent.9.png",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_balloon_bg_right",
            kind = OneUiDrawableKind.Painter,
            api = "OneUiTipPopup right balloon",
            reference = "lib/src/main/res/drawable-desk-mdpi/oui_des_tip_popup_balloon_bg_right.9.png",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_balloon_bg_right_translucent",
            kind = OneUiDrawableKind.Painter,
            api = "OneUiTipPopup translucent right balloon",
            reference = "lib/src/main/res/drawable-desk-mdpi/oui_des_tip_popup_balloon_bg_right_translucent.9.png",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_btn_borderless",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiTipPopup borderless button",
            reference = "lib/src/main/res/drawable/oui_des_tip_popup_btn_borderless.xml",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_hint_bg01",
            kind = OneUiDrawableKind.Painter,
            api = "OneUiTipPopup hint background 01",
            reference = "lib/src/main/res/drawable-desk-mdpi/oui_des_tip_popup_hint_bg01.png",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_hint_bg02",
            kind = OneUiDrawableKind.Painter,
            api = "OneUiTipPopup hint background 02",
            reference = "lib/src/main/res/drawable-desk-mdpi/oui_des_tip_popup_hint_bg02.png",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_hint_bg03",
            kind = OneUiDrawableKind.Painter,
            api = "OneUiTipPopup hint background 03",
            reference = "lib/src/main/res/drawable-desk-mdpi/oui_des_tip_popup_hint_bg03.png",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_hint_bg04",
            kind = OneUiDrawableKind.Painter,
            api = "OneUiTipPopup hint background 04",
            reference = "lib/src/main/res/drawable-desk-mdpi/oui_des_tip_popup_hint_bg04.png",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_hint_bg_translucent",
            kind = OneUiDrawableKind.Painter,
            api = "OneUiTipPopup translucent hint background",
            reference = "lib/src/main/res/drawable-desk-mdpi/oui_des_tip_popup_hint_bg_translucent.png",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_hint_icon",
            kind = OneUiDrawableKind.Painter,
            api = "OneUiTipPopup hint icon",
            reference = "lib/src/main/res/drawable-desk-mdpi/oui_des_tip_popup_hint_icon.png",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_hint_icon_rtl",
            kind = OneUiDrawableKind.Painter,
            api = "OneUiTipPopup RTL hint icon",
            reference = "lib/src/main/res/drawable-desk-mdpi/oui_des_tip_popup_hint_icon_rtl.png",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_hint_icon_translucent",
            kind = OneUiDrawableKind.Painter,
            api = "OneUiTipPopup translucent hint icon",
            reference = "lib/src/main/res/drawable-desk-mdpi/oui_des_tip_popup_hint_icon_translucent.png",
        ),
        referencePrimitive(
            name = "oui_des_tip_popup_hint_icon_translucent_rtl",
            kind = OneUiDrawableKind.Painter,
            api = "OneUiTipPopup translucent RTL hint icon",
            reference = "lib/src/main/res/drawable-desk-mdpi/oui_des_tip_popup_hint_icon_translucent_rtl.png",
        ),
        referencePrimitive(
            name = "oui_des_toast_frame_mtrl",
            kind = OneUiDrawableKind.Shape,
            api = "OneUiSnackbar surface",
            reference = "lib/src/main/res/drawable/oui_des_toast_frame_mtrl.xml",
        ),
    )

    /** Every dependency drawable plus each source-audited design-only mapping implemented so far. */
    val entries: List<OneUiDrawableEntry> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        (dependencyEntries + designEntries + additionalDesignEntries)
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

    /**
     * Resolves a catalog entry to its resource-backed Compose icon when one exists.
     *
     * Stateful, painter, and higher-order reference entries intentionally return `null`; callers
     * should use the entry's rendering metadata for those contracts instead of silently treating
     * them as ordinary icons.
     */
    fun icon(name: String): OneUiIcon? = entries
        .firstOrNull { entry -> entry.name == name }
        ?.rendering
        ?.let { rendering -> (rendering as? OneUiDrawableRendering.Icon)?.icon }

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
        reference: String = DesignReference,
    ) = OneUiDrawableEntry(
        name = name,
        kind = kind,
        source = OneUiDrawableSource.OneUiDesignReference,
        rendering = OneUiDrawableRendering.Primitive(api),
        reference = reference,
    )
}
