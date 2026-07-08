# Asset manifest — Cowlsly Simple Settings

Labeled catalog of all assets in this repo. Each entry maps to a Phase 1 roadmap item where applicable.

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

| File | Labels | Purpose |
|------|--------|---------|
| `simple_settings_app_icon_transparent.svg` | `phase-1`, `icon`, `transparent`, `branding` | Main Simple Settings module icon |
| `simple_settings_volume_steps_icon_transparent.svg` | `phase-1`, `icon`, `transparent`, `volume` | Volume stepped-level control and safe-listening entry point |
| `simple_settings_volume_mute_icon_transparent.svg` | `phase-1`, `icon`, `transparent`, `volume` | Mute toggle control |
| `simple_settings_hearing_warning_icon_transparent.svg` | `phase-1`, `icon`, `transparent`, `volume` | Hearing-damage warning for headphone/earbud use |
| `simple_settings_casmea_info_entry_icon_transparent.svg` | `phase-1`, `icon`, `transparent`, `casmea` | Personal/medical information entry screen |
| `simple_settings_developer_shortcut_icon_transparent.svg` | `phase-1`, `icon`, `transparent`, `developer` | Developer-options shortcut (visible only after access granted) |
| `simple_settings_security_privacy_icon_transparent.svg` | `phase-3`, `icon`, `transparent`, `security`, `privacy` | Security and privacy zone |
| `simple_settings_device_settings_icon_transparent.svg` | `phase-2`, `icon`, `transparent`, `device` | Regular device settings launcher zone |

## UI badges (`assets/images/ui/volume_steps/`)

| File | Labels | Purpose |
|------|--------|---------|
| `simple_settings_volume_step_0_percent_transparent.svg` | `phase-1`, `ui`, `transparent`, `volume` | 0% step indicator (mute level) |
| `simple_settings_volume_step_25_percent_transparent.svg` | `phase-1`, `ui`, `transparent`, `volume` | 25% step indicator |
| `simple_settings_volume_step_50_percent_transparent.svg` | `phase-1`, `ui`, `transparent`, `volume` | 50% step indicator |
| `simple_settings_volume_step_75_percent_transparent.svg` | `phase-1`, `ui`, `transparent`, `volume` | 75% step indicator |
| `simple_settings_volume_step_90_percent_transparent.svg` | `phase-1`, `ui`, `transparent`, `volume` | 90% maximum safe level (no 100% option) |

## Shared branding (`assets/branding/`)

| File | Labels | Purpose |
|------|--------|---------|
| `cowlsly_logo_small.png` | `branding`, `logo`, `cyberpunk` | Official small Cowlsly Production badge for README/docs |
| `cowlsly_logo_small_transparent.svg` | `branding`, `logo`, `cyberpunk`, `transparent` | Compact SVG badge |
| `cowlsly_cogs_background_animated.svg` | `branding`, `background`, `cyberpunk`, `animated` | Forever-turning cog machine background |

See `Cowlsly/BRANDING.md` for suite-wide usage rules.

## Design palette (shared across assets)

- Background: `#17304d` → `#071321` → `#020610`
- Gold accent: `#fff2a8` → `#d7a93d` → `#815b18`
- Cyan accent: `#20f0ff`
- Warning: `#ff5c78` with `#fff2a8` stroke