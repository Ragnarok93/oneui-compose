# One UI 8 Compose design guidelines

This package turns the current `oneui-compose` fork into a reusable Compose-facing design layer instead of requiring projects to manually recreate SESL behavior screen by screen.

## Motion

The primary curve is `CubicBezierEasing(0.22, 0.25, 0, 1)`, matching `oui_des_interpolator_22_25_0_100` in the companion `Ragnarok93/oneui-design` fork. Use `OneUI8Motion` rather than scattering `tween(...)` constants through application code.

Reference timings currently surfaced as tokens:

| Interaction | Duration |
| --- | ---: |
| Direct press response | 110 ms |
| Selection check, first stroke | 110 ms |
| Selection check, second stroke | 180 ms |
| Bottom-sheet enter translation | 300 ms |
| Bottom-sheet enter alpha | 165 ms |
| Bottom-sheet exit translation | 200 ms |
| Bottom-sheet exit alpha | 140 ms |

Prefer short, high-response motion for direct manipulation. Use longer 200–300 ms motion for spatial transitions where the user needs to understand where content came from or went.

The reference bottom-sheet exit resource names Samsung/SESL `sine_out_80`, but the exact curve is not defined in the `oneui-design` source tree. Until that implementation is available under usable terms, `OneUI8Motion.sheetExit()` deliberately uses the verified `0.22, 0.25, 0, 1` curve as a compatibility approximation while preserving the exact 200/140 ms durations.

## Shape and spacing

Reusable defaults are exposed through `OneUI8Theme.dimensions`.

- 24 dp screen-side padding for normal phone layouts.
- 20 dp major section rhythm.
- 26 dp large card/category radius.
- 22 dp nested control/card radius.
- 48 dp minimum touch target.
- 24 dp standard icon grid and 20 dp compact icon grid.
- 46 dp compact floating action/navigation surface height where appropriate.

Prefer nested radii: a child surface should normally be slightly tighter than the parent instead of repeating the same corner radius at every level.

## Iconography

`OneUI8Icons` contains original vectors authored for this fork. They use a 24×24 optical grid, rounded line caps/joins, restrained stroke weight, and centered silhouettes suitable for 20–24 dp rendering.

The public Samsung/Figma design kit should be treated as a visual guideline unless the exact asset's redistribution terms are known. Do not copy/export proprietary Samsung vectors into this repository by default. When a project needs an exact brand-owned asset, keep it in that project's licensed asset pipeline rather than in the generic library.

## Components

The first One UI 8 component layer includes:

- `OneUI8Button` and `OneUI8IconButton`
- `OneUI8Switch`
- `OneUI8Slider`
- `OneUI8Card` and `OneUI8Section`
- `OneUI8ListItem`
- `OneUI8SelectionIndicator`
- `OneUI8NavigationBar`

All interactive components should share the same motion vocabulary. Avoid component-specific spring/tween constants unless a SESL reference demonstrates a distinct motion family.

## Integration

Wrap new screens in `OneUI8Theme`. Existing `OneUITheme` consumers remain valid; the One UI 8 layer is additive and internally bridges the legacy color theme into a Material 3 `ColorScheme` for interoperability with Compose components.
