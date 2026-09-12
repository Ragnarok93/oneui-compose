#!/usr/bin/env python3
"""Static One UI 8 sample parity audit.

The checked-in manifests are intentionally allowed to contain missing/in-progress items while the
parity project is underway. `--strict` is the release/completion gate and rejects every unresolved
route, reference item, drawable and motion mapping.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[1]
REFERENCE = ROOT / "parity" / "reference.json"
DRAWABLES = ROOT / "parity" / "drawables.txt"
MOTION = ROOT / "parity" / "motion.json"
ALLOWED_STATUS = {"done", "mapped", "in_progress", "missing"}
REFERENCE_SHA = "ce4f2cae8c0d712acd2f0b2926ee909fd5d8a434"


def fail(message: str) -> None:
    print(f"PARITY ERROR: {message}", file=sys.stderr)
    raise SystemExit(1)


def load_json(path: Path):
    if not path.exists():
        fail(f"missing manifest: {path.relative_to(ROOT)}")
    try:
        return json.loads(path.read_text(encoding="utf-8"))
    except json.JSONDecodeError as exc:
        fail(f"invalid JSON in {path.relative_to(ROOT)}: {exc}")


def audit_reference(strict: bool) -> tuple[int, int]:
    data = load_json(REFERENCE)
    if data.get("referenceSha") != REFERENCE_SHA:
        fail("reference.json is not pinned to the approved oneui-design SHA")

    records = list(data.get("routes", [])) + list(data.get("nested", []))
    ids: set[str] = set()
    unresolved = 0
    for record in records:
        ident = record.get("id", "")
        if not ident or ident in ids:
            fail(f"route id is empty or duplicated: {ident!r}")
        ids.add(ident)
        for field in ("label", "publicApi", "demoTag", "status"):
            if not record.get(field):
                fail(f"route {ident} missing {field}")
        if record["status"] not in ALLOWED_STATUS:
            fail(f"route {ident} has invalid status {record['status']}")
        if record["status"] not in {"done", "mapped"}:
            unresolved += 1

    item_ids: set[str] = set()
    for item in data.get("referenceItems", []):
        ident = item.get("id", "")
        if not ident or ident in item_ids:
            fail(f"reference item id is empty or duplicated: {ident!r}")
        item_ids.add(ident)
        if item.get("status") not in ALLOWED_STATUS:
            fail(f"reference item {ident} has invalid status")
        if item.get("status") not in {"done", "mapped"}:
            unresolved += 1

    if strict and unresolved:
        fail(f"{unresolved} route/reference items are unresolved")
    return len(records) + len(item_ids), unresolved


def audit_drawables(strict: bool) -> tuple[int, int]:
    if not DRAWABLES.exists():
        fail("missing parity/drawables.txt")
    names: set[str] = set()
    unresolved = 0
    count = 0
    for raw in DRAWABLES.read_text(encoding="utf-8").splitlines():
        line = raw.strip()
        if not line or line.startswith("#"):
            continue
        parts = line.split("|")
        if len(parts) != 4:
            fail(f"malformed drawable entry: {line}")
        name, kind, mapping, status = parts
        if name in names:
            fail(f"duplicate drawable mapping: {name}")
        names.add(name)
        if not kind or not mapping or status not in ALLOWED_STATUS:
            fail(f"invalid drawable mapping: {line}")
        count += 1
        if status not in {"done", "mapped"}:
            unresolved += 1
    if strict and unresolved:
        fail(f"{unresolved} drawable mappings are unresolved")
    return count, unresolved


def audit_motion(strict: bool) -> tuple[int, int]:
    data = load_json(MOTION)
    if data.get("referenceSha") != REFERENCE_SHA:
        fail("motion.json is not pinned to the approved oneui-design SHA")
    ids: set[str] = set()
    unresolved = 0
    mappings = data.get("mappings", [])
    for record in mappings:
        ident = record.get("id", "")
        if not ident or ident in ids:
            fail(f"motion id is empty or duplicated: {ident!r}")
        ids.add(ident)
        for field in ("source", "mapping", "status"):
            if not record.get(field):
                fail(f"motion {ident} missing {field}")
        if record["status"] not in ALLOWED_STATUS:
            fail(f"motion {ident} has invalid status")
        if record["status"] not in {"done", "mapped"}:
            unresolved += 1
    if strict and unresolved:
        fail(f"{unresolved} motion mappings are unresolved")
    return len(mappings), unresolved


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--strict", action="store_true", help="require zero unresolved parity items")
    parser.add_argument("--check-drawables", action="store_true")
    parser.add_argument("--check-motion", action="store_true")
    parser.add_argument("--check", action="store_true", help="validate all checked-in manifests")
    args = parser.parse_args()

    check_all = args.check or not (args.check_drawables or args.check_motion)
    total = 0
    unresolved = 0
    if check_all:
        c, u = audit_reference(args.strict)
        total += c
        unresolved += u
    if check_all or args.check_drawables:
        c, u = audit_drawables(args.strict)
        total += c
        unresolved += u
    if check_all or args.check_motion:
        c, u = audit_motion(args.strict)
        total += c
        unresolved += u

    print(f"One UI 8 parity audit: {total} mapped records, {unresolved} unresolved")
    if args.strict and unresolved:
        raise SystemExit(1)


if __name__ == "__main__":
    main()
