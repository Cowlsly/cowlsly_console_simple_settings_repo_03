# ROADMAP — cowlsly_console_simple_settings_repo_03

## Status
**Phase 1 complete** — Android app shipped in `app/`; volume, CASMEA entry, and developer gate live inside the full paged catalog with search, usage ranking, and suite sync.

## North-star vision
See `dev/dat/doc/SIMPLE_SETTINGS_VISION.md` for the full master settings app design: one scroll, closest settings first, then security & privacy, all regular device settings, and gated developer options.

**Simple** means the settings you need most are **closest to you** — not that the app is stripped down.

## Latest Build Log
- 2026-07-08 — Full catalog available: expanded system settings (USB, print, network operators, bubbles, WebView, exact alarms, …). List order = highest base priority first, most-used within each zone. Usage recorded on every panel interaction.
- 2026-07-08 — **Phase 1 complete.** Verified debug build (`assembleDebug` with Java 21). All source, docs, and Gradle scaffold pushed to remote.
- 2026-07-08 — Scaffolded Android app (`app/`): Jetpack Compose, paged glass panels over animated cogs, search bar, usage/search ranking, 50+ system setting intents, Shizuku hook, CASMEA content provider, credits page.
- 2026-07-08 — Page 1 pinned Cowlsly Console Settings logo button → `https://cowlsly.com/console/settings`.
- 2026-07-08 — Suite sync: every setting change → Android source + local JSON + cowlsly.com API + suite ContentProvider broadcast. Pull on resume for coherence.

## Latest Asset / Documentation Log
- 2026-07-08 — Added `assets/images/icons/simple_settings_volume_steps_icon_transparent.svg` as the first real transparent Simple Settings icon. Added `assets/README.md` with suite-safe asset naming rules.
- 2026-07-08 — Completed Phase 1 asset set: module icon, volume/mute/warning icons, CASMEA entry icon, developer shortcut icon, and five volume-step UI badges (0–90%). Added `assets/ASSET_MANIFEST.md` with labels and phase mapping.
- 2026-07-08 — Added shared Cowlsly branding kit (`assets/branding/`) with small logo, forever-turning cogs background SVG, and `Cowlsly/BRANDING.md` suite standard.
- 2026-07-08 — Added `dev/dat/doc/SIMPLE_SETTINGS_VISION.md` (master settings hub design). Added security/privacy and device-settings zone icons.

## Purpose
A single, shared **master settings module** that other Cowlsly apps point to or include — volume, security, privacy, personal info, every regular device setting category, and developer options (when access is granted).

## Settings zones (priority scroll)

| P | Zone | Phase |
|---|------|-------|
| 1 | Closest: volume, mute, panel tint, hearing warning | **1** |
| 2 | Sound & display shortcuts | 2 |
| 3 | Security: PIN, password, biometrics, lock behaviour | 3 |
| 4 | Privacy: permissions hub, privacy dashboard links | 3 |
| 5 | Personal info + CASMEA medical entry | **1** (entry), 3 (full profile) |
| 6 | Network & connectivity (system intents) | 2 |
| 7 | Device: storage, battery, language, accessibility | 2 |
| 8 | Apps & default apps | 2 |
| 9 | Notifications | 2 |
| 10 | Accounts & backup | 2 |
| 11 | Developer options (gated; opens when access granted) | **1** (stub), 4 (live) |
| 12 | About & website sync | 5 |

## Phase 1 Scope (complete)
- **A) Volume control**: mute, plus stepped levels (0% / 25% / 50% / 75% / 90%) — no 100% option, with a built-in hearing-damage warning for headphone/earbud use.
- **B) Base information entry screen**: the single source of truth for personal/medical info displayed by `cowlsly_console_medical_emergency_assistant_casmea_repo_02`. Note: the CASMEA app itself has no data-entry screen — entry only happens here, for security.
- **C) Developer options shortcut**: a fast-access button to developer tools, visible only if developer access has already been explicitly granted. PIN/password re-entry required each visit.

## Later phases (from vision doc)
- **Phase 2** — System intent launcher for all regular device settings (network, display, apps, notifications, accounts).
- **Phase 3** — Full security & privacy hub wired to suite lock model.
- **Phase 4** — Live developer gate + `APPLICATION_DEVELOPMENT_SETTINGS` links.
- **Phase 5** — Website sync JSON for console + profile fields.
- **Phase 6** — Embeddable module SDK for Vault, CASMEA, Cowlsly.com.

## Explicitly Out of Scope
- Storing ALI-Key API secrets (stays in Vault keyring).
- Duplicating Android's private settings storage — we **lead** users to system screens.

## Dependencies
- Consumed by `cowlsly_console_medical_emergency_assistant_casmea_repo_02` and, later, `cowlsly_console_authenticator_repo_04`.
- Aligns with Vault `docs/cowlsly_settings_control.md` (layer 1: console UI).

## Current Asset Notes
- `assets/README.md` — asset naming, folder layout, and reuse notes.
- `assets/ASSET_MANIFEST.md` — labeled catalog mapping every asset to scope.
- `assets/branding/` — shared Cowlsly small logo + forever-turning cogs background.
- `Cowlsly/BRANDING.md` — suite-wide rules for logo embeds and cyberpunk backgrounds.
- `assets/images/icons/` — eight transparent SVG zone icons.
- `assets/images/ui/volume_steps/` — five transparent step badges (0%, 25%, 50%, 75%, 90%).

## Related Documents
- `dev/dat/doc/SIMPLE_SETTINGS_VISION.md` — full master settings app vision.
- `dev/dat/doc/ROADMAP.ORIGINAL.md` — founding context and rebuild notes.