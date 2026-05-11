#!/usr/bin/env bash
# ── Quick build helpers (use from Git Bash / WSL on Windows) ─────────────────
set -euo pipefail

TASK="${1:-assembleDebug}"

echo "==> Building: $TASK"
docker compose run --rm android ./gradlew "$TASK" --no-daemon

echo ""
echo "==> APK output (if applicable):"
find app/build/outputs -name "*.apk" 2>/dev/null || true
