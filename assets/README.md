# Cowlsly Simple Settings assets

This folder holds real UI and icon assets for `cowlsly_console_simple_settings_repo_03`.

## Naming rules

- Use `lower_snake_case` file names.
- Prefix with `simple_settings_` for suite-wide reuse.
- Suffix transparent assets with `_transparent`.
- Transparent UI pieces should be PNG or SVG.
- Assets should remain reusable across the Cowlsly suite where possible.

## Folder layout

```
assets/
├── ASSET_MANIFEST.md          # Labeled catalog (labels, purpose, phase mapping)
├── README.md                  # This file
├── branding/                  # Shared Cowlsly logo + forever-turning cogs background
└── images/
    ├── icons/                 # 512×512 control and module icons
    └── ui/
        └── volume_steps/      # Stepped volume level badges (0–90%)
```

## Phase 1 asset catalog

See `ASSET_MANIFEST.md` for the full labeled index. Summary by roadmap scope:

### A) Volume control

| Asset | Path |
|-------|------|
| Module volume steps icon | `images/icons/simple_settings_volume_steps_icon_transparent.svg` |
| Mute control icon | `images/icons/simple_settings_volume_mute_icon_transparent.svg` |
| Hearing-damage warning icon | `images/icons/simple_settings_hearing_warning_icon_transparent.svg` |
| Step badges (0 / 25 / 50 / 75 / 90%) | `images/ui/volume_steps/simple_settings_volume_step_*_percent_transparent.svg` |

### B) CASMEA information entry

| Asset | Path |
|-------|------|
| Info entry screen icon | `images/icons/simple_settings_casmea_info_entry_icon_transparent.svg` |

### C) Developer options shortcut

| Asset | Path |
|-------|------|
| Developer shortcut icon | `images/icons/simple_settings_developer_shortcut_icon_transparent.svg` |

### D) Security, privacy & device (vision phases 2–3)

| Asset | Path |
|-------|------|
| Security & privacy zone icon | `images/icons/simple_settings_security_privacy_icon_transparent.svg` |
| Device settings zone icon | `images/icons/simple_settings_device_settings_icon_transparent.svg` |

### Branding

| Asset | Path |
|-------|------|
| Main module icon | `images/icons/simple_settings_app_icon_transparent.svg` |

## Shared branding

Every Cowlsly repo carries `assets/branding/` (small logo + forever-turning cogs background). See `Cowlsly/BRANDING.md`.

## Notes

Simple Settings is the shared settings/data-entry module. CASMEA medical information entry belongs here rather than inside the CASMEA emergency-view app itself.