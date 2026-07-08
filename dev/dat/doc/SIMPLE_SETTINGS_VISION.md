# Simple Settings — master settings app vision

**Repo:** `cowlsly_console_simple_settings_repo_03`  
**Last updated:** 2026-07-08  
**Status:** North-star design — Phase 1 builds the first slice; this doc holds the full picture.

## Why it is called Simple Settings

**Simple** does not mean *limited*. It means the settings you need most are **closest to you** — at the top, in one place, in priority order — so you never dig through five Android menus to change volume, privacy, or security.

One endless scroll. Most-used controls first. Everything else still there, just below. No hunting.

## One app, every layer

Simple Settings is the **master settings hub** for the Cowlsly Console suite and the device it runs on.

| Layer | What Simple Settings owns |
|-------|---------------------------|
| **Closest** | Volume, mute, panel tint, brightness feel, quick sound/display tweaks |
| **Console** | Cowlsly look-and-feel shared across Vault, CASMEA, Cowlsly.com |
| **Personal** | Profile basics, CASMEA medical info entry (single source of truth) |
| **Security & privacy** | Lock methods, biometrics, permissions overview, privacy shortcuts |
| **Device** | Every regular Android settings category — led from one menu |
| **Developer** | Gated section; opens Developer Options when access is granted |

Other Cowlsly apps **include or deep-link** into Simple Settings instead of rebuilding their own settings screens.

## Design feel

- Cyberpunk neon glass panels over the **forever-turning cog machine** background.
- Cowlsly small logo in headers. See `Cowlsly/BRANDING.md`.
- Calm readability: security and privacy sections stay clear, not decorative clutter.
- Page-turner navigation optional on large screens; phone uses one vertical scroll.

## The settings scroll — priority order

Top = closest to you. Bottom = deeper system layers. **Never reorder without updating this doc and `SettingsPriority`.**

| P | Zone | What the user gets |
|---|------|-------------------|
| **1** | **Closest to you** | Volume (mute + 0/25/50/75/90% steps, hearing warning), panel colour tint, quick brightness feel |
| **2** | **Sound & display** | Media volume link, notification tone link, display/text size shortcuts |
| **3** | **Security** | PIN, password, fingerprint toggles; lock-on-background; screen lock timeout link |
| **4** | **Privacy** | Permissions hub, location/camera/mic overview, privacy dashboard link, app data controls |
| **5** | **Personal info** | Username, email, avatar; **CASMEA medical entry** (edits only here, not in CASMEA app) |
| **6** | **Network & connectivity** | Wi‑Fi, mobile data, Bluetooth, NFC, hotspot — system intents |
| **7** | **Device** | Storage, battery, date/time, language, accessibility — system intents |
| **8** | **Apps** | Installed apps list, default apps, special access — system intents |
| **9** | **Notifications** | Per-app notification settings, Do Not Disturb link |
| **10** | **Accounts & backup** | Google/system accounts, backup, sync status links |
| **11** | **Developer** | Hidden until access granted; PIN/password re-entry; then Developer Options + USB debugging links |
| **12** | **About & sync** | App version, Cowlsly identity, website settings JSON export/import |

```mermaid
flowchart TD
    A[Open Simple Settings] --> B[Forever-turning cogs background + glass panel]
    B --> C[P1 Closest: volume / tint / quick controls]
    C --> D[P2–P5: sound, security, privacy, personal + CASMEA]
    D --> E[P6–P10: device, network, apps, notifications, accounts]
    E --> F{Developer access granted?}
    F -- No --> G[P12 About & sync]
    F -- Yes --> H[P11 PIN/password gate]
    H --> I[Developer Options + system dev settings]
    I --> G
```

## Security & privacy — in detail

### In-app (Simple Settings stores or toggles)

- PIN / password / biometric enablement
- Lock when app goes to background
- Clipboard auto-clear preference (suite-wide)
- CASMEA field visibility flags (what shows on lock-screen emergency card)
- Developer section visibility flag (off by default)

### System-led (intents — we open Android settings, not duplicate them)

| Category | Android entry |
|----------|---------------|
| Screen lock type | `Settings.ACTION_SECURITY_SETTINGS` |
| App permissions | `Settings.ACTION_APPLICATION_DETAILS_SETTINGS` per app |
| Privacy dashboard | `Settings.ACTION_PRIVACY_SETTINGS` (API 30+) |
| Location | `Settings.ACTION_LOCATION_SOURCE_SETTINGS` |
| Notification policy | `Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS` |

Simple Settings **surfaces** these in plain language with Cowlsly icons. It does not re-implement Android's private settings storage.

## Developer options — how they open

Developer controls are **never** on the main scroll by default.

### Step 1 — Grant developer access (one-time)

User enables **Developer access** inside Simple Settings → About. Requires PIN or password. Sets `developer_access_granted = true` locally.

> On many devices the user must also tap **Build number ×7** in Android About phone. Simple Settings shows that instruction and a direct link.

### Step 2 — Open developer section

After access is granted, **P11 Developer** appears in the scroll. Opening it requires **fresh PIN/password re-entry** (same rule as Vault key protection).

### Step 3 — Links inside developer section

| Control | Action |
|---------|--------|
| Developer Options | `Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS` |
| USB debugging | Opens dev settings (user toggles there) |
| Wireless debugging | Link when API ≥ 30 |
| Cowlsly dev tools | Shortcut to internal debug screens (suite apps only) |

The developer shortcut icon (`simple_settings_developer_shortcut_icon_transparent.svg`) is used for this zone only.

## Regular device settings — full catalog

Simple Settings lists **every standard Android settings category** as a labelled row with icon. Tapping opens the system screen.

| Group | Examples |
|-------|----------|
| **Wireless** | Wi‑Fi, mobile network, aeroplane mode, data saver |
| **Connected devices** | Bluetooth, cast, NFC, USB |
| **Sound** | Volume, vibration, default notification sound |
| **Display** | Brightness, dark theme, font size, screen timeout |
| **Wallpaper & style** | Wallpaper, colour palette (system) |
| **Storage** | Free space, app storage, trash |
| **Battery** | Usage, battery saver, adaptive battery |
| **Apps** | All apps, default apps, unused apps |
| **Notifications** | App list, Bubbles, history |
| **Privacy** | Permission manager, privacy dashboard |
| **Location** | App location access |
| **Security** | Screen lock, encryption, trust agents |
| **Accounts** | Google and other accounts |
| **System** | Languages, date/time, gestures, backup |
| **Accessibility** | TalkBack, display size, captions |
| **Tips & support** | Help, feedback (links) |

Nothing is removed to keep the app "simple" — it is **organised**, not stripped down.

## Suite integration

| Consumer | What it pulls from Simple Settings |
|----------|-----------------------------------|
| **Cowlsly Vault** | P1 console UI, P3 security toggles, P11 developer gate pattern |
| **CASMEA** | P5 medical info fields (read-only in CASMEA app; edit only here) |
| **Cowlsly.com / home** | Console UI prefs + profile/security flags via `cowlsly_website_settings_sync.json` |
| **Authenticator** (future) | Identity + security layer from P3–P5 |

Vault keeps **ALI-Key secrets** in the keyring. Simple Settings never stores API keys.

## Phase map

| Phase | Delivers |
|-------|----------|
| **1 (now)** | Volume + hearing warning, CASMEA entry screen, developer shortcut stub |
| **2** | Full P1–P2 scroll, system intent launcher for P6–P8 |
| **3** | Security & privacy hub (P3–P4) wired to suite lock model |
| **4** | Developer gate + P11 live links |
| **5** | Website sync JSON for console + profile fields |
| **6** | Embedded module SDK for other Cowlsly apps (Compose / intent) |

## Screen sketch (phone)

```text
┌─────────────────────────────────────┐
│  [Cowlsly logo]   Simple Settings   │
├─────────────────────────────────────┤
│  ▓▓ cogs background (looping) ▓▓    │
│  ┌─────────────────────────────┐    │
│  │ P1  VOLUME  [mute][====●--] │    │
│  │     ⚠ hearing-safe steps    │    │
│  │ P1  PANEL TINT  [slider]    │    │
│  ├─────────────────────────────┤    │
│  │ P3  SECURITY                │    │
│  │     PIN / password / finger │    │
│  ├─────────────────────────────┤    │
│  │ P4  PRIVACY                 │    │
│  │     Permissions  →          │    │
│  ├─────────────────────────────┤    │
│  │ P5  CASMEA INFO ENTRY  →    │    │
│  ├─────────────────────────────┤    │
│  │ P6  NETWORK & DEVICE  →     │    │
│  │     Wi‑Fi / Bluetooth / …   │    │
│  ├─────────────────────────────┤    │
│  │ P11 DEVELOPER (gated)  →    │    │
│  └─────────────────────────────┘    │
│         ↓ endless scroll ↓          │
└─────────────────────────────────────┘
```

## Rules for builders and Marla

1. **Closest first** — if a setting is used daily, it belongs in P1–P2.
2. **Do not duplicate Android** — lead the user to system settings for device-private data.
3. **Protect mutations** — PIN/password/biometric required before security, personal, or developer changes.
4. **CASMEA edits only here** — emergency app is view-only on lock screen.
5. **Developer is gated twice** — grant access once, re-enter PIN each visit.
6. **Update docs from the app** — when implementation changes, update this file and `ROADMAP.md`.

## Related documents

- `ROADMAP.md` — active build phases
- `dev/dat/doc/ROADMAP.ORIGINAL.md` — founding context
- Vault `docs/cowlsly_settings_control.md` — three settings layers
- Vault `docs/settings_priority_memory.md` — priority rules (align P6–P9 with this vision)