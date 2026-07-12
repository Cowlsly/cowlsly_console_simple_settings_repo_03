# Historical asset manifest — Cowlsly Simple Settings

This catalog records the Phase 1 local paths that existed before canonical UI migration. Reusable masters belong in `Cowlsly/cowlsly-ui-assets-and-data-files`; these entries are migration mappings or generated consumer copies, not an independent source of truth.

## Labels

| Label | Meaning |
|-------|---------|
| `phase-1` | Required for Phase 1 build |
| `icon` | Square app/control icon (512×512) |
| `ui` | Inline UI badge or control strip |
| `transparent` | Transparent background (SVG) |
| `volume` | Volume control feature (scope A) |
| `casmea` | CASMEA info entry feature (scope B) |
| `developer` | Developer shortcut feature (scope C) |
| `branding` | Module identity / launcher icon |

## Icons (`assets/images/icons/`)

| Local file | Labels | Purpose | Canonical destination |
|---|---|---|---|
| `simple_settings_app_icon_transparent.svg` | `phase-1`, `icon`, `transparent`, `branding` | Main module icon | `assets/ui/icons/simple_settings/` |
| `simple_settings_volume_steps_icon_transparent.svg` | `phase-1`, `icon`, `transparent`, `volume` | Volume stepped-level control | `assets/ui/icons/simple_settings/` |
| `simple_settings_volume_mute_icon_transparent.svg` | `phase-1`, `icon`, `transparent`, `volume` | Mute toggle | `assets/ui/icons/simple_settings/` |
| `simple_settings_hearing_warning_icon_transparent.svg` | `phase-1`, `icon`, `transparent`, `volume` | Hearing warning | `assets/ui/icons/simple_settings/` |
| `simple_settings_casmea_info_entry_icon_transparent.svg` | `phase-1`, `icon`, `transparent`, `casmea` | CASMEA entry | `assets/ui/icons/simple_settings/` |
| `simple_settings_developer_shortcut_icon_transparent.svg` | `phase-1`, `icon`, `transparent`, `developer` | Developer shortcut | `assets/ui/icons/simple_settings/` |
| `simple_settings_security_privacy_icon_transparent.svg` | `phase-3`, `icon`, `transparent`, `security`, `privacy` | Security/privacy zone | `assets/ui/icons/simple_settings/` |
| `simple_settings_device_settings_icon_transparent.svg` | `phase-2`, `icon`, `transparent`, `device` | Device settings zone | `assets/ui/icons/simple_settings/` |

## UI badges (`assets/images/ui/volume_steps/`)

| Local file | Labels | Purpose | Canonical destination |
|---|---|---|---|
| `simple_settings_volume_step_0_percent_transparent.svg` | `phase-1`, `ui`, `transparent`, `volume` | 0% / mute | `assets/ui/controls/simple_settings/volume_steps/` |
| `simple_settings_volume_step_25_percent_transparent.svg` | `phase-1`, `ui`, `transparent`, `volume` | 25% | `assets/ui/controls/simple_settings/volume_steps/` |
| `simple_settings_volume_step_50_percent_transparent.svg` | `phase-1`, `ui`, `transparent`, `volume` | 50% | `assets/ui/controls/simple_settings/volume_steps/` |
| `simple_settings_volume_step_75_percent_transparent.svg` | `phase-1`, `ui`, `transparent`, `volume` | 75% | `assets/ui/controls/simple_settings/volume_steps/` |
| `simple_settings_volume_step_90_percent_transparent.svg` | `phase-1`, `ui`, `transparent`, `volume` | 90% safe maximum | `assets/ui/controls/simple_settings/volume_steps/` |

## Legacy shared branding (`assets/branding/`)

| Local file | Purpose | Canonical destination |
|---|---|---|
| `cowlsly_logo_small.png` | Documentation badge | `assets/branding/cowlsly/compact/` |
| `cowlsly_logo_small_transparent.svg` | Compact transparent badge | `assets/branding/cowlsly/compact/` |
| `cowlsly_cogs_background_animated.svg` | Animated cog-machine background | `assets/ui/backgrounds/cowlsly_machine/` |

The old suite policy requiring every repository to carry this folder is obsolete. Local copies may remain only as checksum-verified generated files.

## Palette

- Background: `#17304d` → `#071321` → `#020610`
- Gold accent: `#fff2a8` → `#d7a93d` → `#815b18`
- Cyan accent: `#20f0ff`
- Warning: `#ff5c78` with `#fff2a8` stroke

See `../UI_ASSET_SOURCE.md` and `../ui_asset_dependencies.json` before changing any asset path.
