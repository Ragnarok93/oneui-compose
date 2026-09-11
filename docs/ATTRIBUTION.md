# Attribution and provenance

The One UI 8 additions are original Compose implementations for the `Ragnarok93/oneui-compose` fork.

Behavioral and resource-value reference:

- `Ragnarok93/oneui-design` / OneUIProject-derived Android XML library, MIT licensed.
- SESL-style curve `0.22, 0.25, 0, 1` is represented in that project as `oui_des_interpolator_22_25_0_100`.
- Bottom-sheet durations and selection-check timing are adapted as implementation constants from the corresponding MIT-licensed resource definitions.
- The bottom-sheet exit reference names `sine_out_80`, whose exact implementation is not present in that source tree; the Compose port documents and uses a verified-curve approximation rather than claiming an exact port of that easing.

The vector paths in `OneUI8Icons.kt` were newly authored for this repository. They are not exports of Samsung's Figma kit and are intentionally kept generic and redistributable under this repository's license.
