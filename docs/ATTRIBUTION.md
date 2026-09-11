# Attribution and provenance

The One UI 8 additions are original Compose implementations for the `Ragnarok93/oneui-compose` fork.

## Behavioral and motion references

- `Ragnarok93/oneui-design` / OneUIProject-derived Android XML library is used as the pinned behavioral and resource-value reference at commit `c9225f3c07bac88f43f3bf2f100963133e27199a`.
- The standard curve `0.22, 0.25, 0, 1` is represented there as `oui_des_interpolator_22_25_0_100`.
- Bottom-sheet durations and selection-check timing are adapted as implementation constants from the corresponding MIT-licensed resource definitions.
- SESL sine curve control points used by `OneUiEasing` were verified against `SeslAnimationUtils` in `OneUIProject/oneui-core` at commit `8c7734aeb9c89cdd74f814e96952709515370142`; the Compose implementation expresses those path-interpolator control points directly as cubic Bézier easings.

## Icon resources

The stable `OneUiIcons` catalog uses drawable resources directly from:

- Maven artifact: `io.github.oneuiproject:icons:1.1.0`
- Source project: `OneUIProject/oneui-icons`
- Upstream repository license: MIT

The dependency was inventoried before mapping public names. Resource-backed entries are referenced through the dependency's generated `dev.oneuiproject.oneui.R` class rather than copying vector XML into this repository.

No matching Cut or Paste drawable was found during the inventory, so the stable catalog does not disguise unrelated dependency artwork as those actions.

`RadioSelected` and `RadioUnselected` use small original fallback vectors authored for this Compose library because the resolved icon artifact does not expose a matching radio-button pair.

The compatibility vectors retained in `OneUI8Icons.kt` were newly authored for this repository. They are not exports of Samsung's Figma kit and are intentionally preserved only to avoid breaking the historical public `ImageVector` return type. New code should use `OneUiIcons` and `OneUiAnimatedIcons`.
