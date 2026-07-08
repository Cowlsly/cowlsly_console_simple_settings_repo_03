# Table of contents

## Branding

* [BRANDING](README.md)
* [assets/branding/README](../assets/branding/README.md)

## Project docs

* [README](../README.md)
* [SIMPLE\_SETTINGS\_VISION](../dev/dat/doc/SIMPLE_SETTINGS_VISION.md)
* [ROADMAP](../ROADMAP.md)
* [ROADMAP.ORIGINAL](../dev/dat/doc/ROADMAP.ORIGINAL.md)

## Assets

* [assets/README](../assets/README.md)
* [ASSET\_MANIFEST](../assets/ASSET_MANIFEST.md)

## UI badges (assets/images/ui/volume\_steps/)

## Application (`app/`)

* `app/build.gradle.kts` — module config (Compose, Shizuku, DataStore)
* `app/src/main/AndroidManifest.xml` — launcher, CASMEA + suite sync providers
* `app/src/main/java/com/cowlsly/simplesettings/`
  * `data/` — `SettingsCatalog`, `CasmeaContentProvider`, `SuiteSyncContentProvider`
  * `sync/` — suite sync engine, website client, permission catalog
  * `ui/` — Compose root, panels, glass components, Cowlsly theme
  * `account/` — Cowlsly.com AccountManager + SyncAdapter stub
* `REPORT.md` — Phase 1 completion summary and build instructions
