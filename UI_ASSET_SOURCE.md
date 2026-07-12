# Cowlsly canonical UI asset source

Reusable Cowlsly UI assets are owned by `Cowlsly/cowlsly-ui-assets-and-data-files` on active branch `main`. Simple Settings is a consumer and must not carry an independently maintained shared-branding master pack.

## Canonical paths consumed

- `assets/branding/cowlsly/compact/`
- `assets/ui/backgrounds/cowlsly_machine/`
- `assets/ui/icons/simple_settings/`
- `assets/ui/controls/simple_settings/volume_steps/`
- `assets/audio/ui/`
- `assets/ui_transitions/cowlsly_vault_transition/`

The Simple Settings SVG icons, five volume-step controls, compact Cowlsly logo, and animated cog background are now canonical in the UI repository. Their existing local SVG paths are declared generated copies in `ui_asset_dependencies.json`.

Binary PNG/WebP exports and Android runtime resources remain protected migration candidates until canonical transfer, checksum reconciliation, reference checks, and a fresh Java 21 Gradle build pass.

## Synchronisation

The pinned dependency contract is `ui_asset_dependencies.json`. With sibling checkouts:

```bash
python ../cowlsly-ui-assets-and-data-files/tools/sync_ui_assets.py \
  --canonical-root ../cowlsly-ui-assets-and-data-files \
  --consumer-root .
```

Generated Android resources and generated SVG copies must not be edited manually. The sync receipt `.cowlsly_ui_generated.json` records source paths, hashes, sizes, and the canonical commit.

## Updating

1. Update the canonical asset and checksum manifest on `main`.
2. Pin the new immutable canonical commit.
3. Synchronise generated resources.
4. Build with Java 21 using `./gradlew assembleDebug`.
5. Verify launcher/adaptive icons, drawables, raw audio, SVG copies, and Compose references.
6. Remove obsolete local masters only after the build passes.

Simple Settings-specific source code, schemas, and documentation remain here. Reusable interface artwork belongs in the UI repository.
