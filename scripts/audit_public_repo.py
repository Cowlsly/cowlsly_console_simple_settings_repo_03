#!/usr/bin/env python3
"""Audit the public repository for assets, exact duplicates, and redacted secret/privacy indicators."""
from __future__ import annotations

import hashlib
import json
import os
import re
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUTPUT = ROOT / "build" / "reports" / "public_repo_audit.json"
SKIP_DIRS = {".git", ".gradle", "build", ".idea"}
TRACKED_EXTENSIONS = {
    ".png", ".jpg", ".jpeg", ".webp", ".bmp", ".gif", ".svg", ".ico",
    ".wav", ".ogg", ".mp3", ".mp4", ".json", ".xml", ".yaml", ".yml",
    ".toml", ".csv", ".md", ".txt", ".kt", ".kts", ".java", ".properties",
}
PATTERNS = {
    "private_key": re.compile(rb"-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----"),
    "github_token": re.compile(rb"\bgh[pousr]_[A-Za-z0-9_]{20,}\b"),
    "secret_assignment": re.compile(
        rb"(?i)\b(api[_-]?key|secret|token|password)\s*[:=]\s*['\"][^'\"\r\n]{8,}"
    ),
    "possible_email_data": re.compile(rb"(?i)\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}\b"),
}


def sha256(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as handle:
        for chunk in iter(lambda: handle.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def main() -> int:
    files: list[dict[str, object]] = []
    hashes: dict[str, list[str]] = defaultdict(list)
    findings: list[dict[str, str]] = []

    for root, dirs, names in os.walk(ROOT):
        dirs[:] = [name for name in dirs if name not in SKIP_DIRS]
        for name in names:
            path = Path(root) / name
            if path.suffix.lower() not in TRACKED_EXTENSIONS:
                continue

            rel = path.relative_to(ROOT).as_posix()
            value = sha256(path)
            files.append({"path": rel, "byte_size": path.stat().st_size, "sha256": value})
            hashes[value].append(rel)

            if path.stat().st_size <= 2_000_000 and path.suffix.lower() not in {
                ".png", ".jpg", ".jpeg", ".webp", ".mp4"
            }:
                data = path.read_bytes()
                for category, pattern in PATTERNS.items():
                    if pattern.search(data):
                        severity = (
                            "high_review_required"
                            if category != "possible_email_data"
                            else "privacy_review_required"
                        )
                        findings.append({"path": rel, "category": category, "severity": severity})

    duplicates = [
        {"sha256": value, "paths": paths}
        for value, paths in hashes.items()
        if len(paths) > 1
    ]
    payload = {
        "repository": "Cowlsly/cowlsly-console-simple-settings-app",
        "file_count": len(files),
        "total_bytes": sum(int(item["byte_size"]) for item in files),
        "exact_duplicate_groups": duplicates,
        "security_and_privacy_findings": findings,
        "files": sorted(files, key=lambda item: str(item["path"])),
        "note": "Findings contain paths and categories only. Matching values are never emitted.",
    }

    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    OUTPUT.write_text(json.dumps(payload, indent=2) + "\n", encoding="utf-8")
    print(
        f"Audited {len(files)} files; {len(duplicates)} exact duplicate groups; "
        f"{len(findings)} review findings"
    )
    return 1 if findings else 0


if __name__ == "__main__":
    raise SystemExit(main())
