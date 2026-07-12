# Cowlsly canonical UI asset source

Reusable Cowlsly UI assets are owned by `Cowlsly/cowlsly-ui-assets-and-data-files` on the canonical `root` branch. Simple Settings is a consumer and must not carry an independently maintained shared-branding master pack.

## Canonical paths consumed

- `assets/branding/cowlsly/compact/`
- `assets/ui/backgrounds/cowlsly_machine/`
- `assets/ui/icons/simple_settings/`
- `assets/ui/controls/simple_settings/volume_steps/`
- `assets/audio/ui/`
- `assets/ui/transitions/`

Current local paths under `assets/branding/`, `assets/images/icons/`, `assets/images/ui/`, and Android `app/src/main/res/` are migration candidates or generated platform copies. They remain until canonical binaries, checksums, Compose/resource references, and a Java 21 Gradle build are verified.

## Synchronisation

The pinned dependency contract is `ui_asset_dependencies.json`. With sibling checkouts:

```bash
python ../cowlsly-ui-assets-and-data-files/tools/sync_ui_assets.py \
  --canonical-root ../cowlsly-ui-assets-and-data-files \
  --consumer-root .
```

Generated Android resources must not be edited manually. The sync receipt `.cowlsly_ui_generated.json` records source paths, hashes, sizes, and the canonical commit.

## Updating

1. Update the canonical asset and checksum manifest.
2. Pin the new immutable canonical commit.
3. Synchronise generated resources.
4. Build with Java 21 using `./gradlew assembleDebug`.
5. Verify launcher/adaptive icons, drawables, raw audio, and Compose references.
6. Remove obsolete local masters only after the build passes.

Simple Settings-specific source code, schemas, and documentation remain here. Reusable interface artwork belongs in the UI repository.
