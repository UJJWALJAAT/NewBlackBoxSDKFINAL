#!/usr/bin/env bash
set -euo pipefail

VARIANT="${1:-release}"
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
NEWBLACKBOX_DIR="$ROOT_DIR/NewBlackbox"
LOADER_DIR="$ROOT_DIR/Loader"
AAR_SRC="$NEWBLACKBOX_DIR/Bcore/build/outputs/aar/Bcore-release.aar"
AAR_DST="$LOADER_DIR/app/libs/Bcore-release.aar"

if [[ "$VARIANT" != "release" && "$VARIANT" != "debug" ]]; then
  echo "[ERROR] Variant must be 'release' or 'debug'. Got: $VARIANT"
  exit 1
fi

echo "[1/3] Building NewBlackbox SDK AAR..."
(
  cd "$NEWBLACKBOX_DIR"
  ./gradlew :Bcore:assembleRelease
)

if [[ ! -f "$AAR_SRC" ]]; then
  echo "[ERROR] AAR not found at $AAR_SRC"
  exit 1
fi

echo "[2/3] Copying AAR to Loader libs..."
mkdir -p "$(dirname "$AAR_DST")"
cp -f "$AAR_SRC" "$AAR_DST"

echo "[3/3] Building Loader APK ($VARIANT)..."
(
  cd "$LOADER_DIR"
  if [[ "$VARIANT" == "release" ]]; then
    ./gradlew :app:assembleRelease
    APK_PATH="$LOADER_DIR/app/build/outputs/apk/release/app-release.apk"
  else
    ./gradlew :app:assembleDebug
    APK_PATH="$LOADER_DIR/app/build/outputs/apk/debug/app-debug.apk"
  fi

  echo "[DONE] APK: $APK_PATH"
)
