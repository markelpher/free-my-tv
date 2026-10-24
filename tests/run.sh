#!/usr/bin/env bash
# Compile and run the JVM-only regression tests (no Android device or Gradle required).
# This is a suite count, not line/branch coverage or an Android UI test.
set -euo pipefail
cd "$(dirname "$0")/.."

OUT="out/tests"
rm -rf "$OUT"
mkdir -p "$OUT"
trap 'rm -rf "$OUT"' EXIT

# Keep this list limited to pure helpers; Android-dependent classes need a separate test harness.
helpers=(DashboardTheme StartupBrand)
sources=()
for helper in "${helpers[@]}"; do sources+=("app/src/io/freemytv/$helper.java"); done

# Discover each *Test.java so a new suite cannot silently be left out of CI.
tests=(tests/java/io/freemytv/*Test.java)
if [ ! -f "${tests[0]}" ]; then
  echo "No JVM test suites found." >&2
  exit 1
fi
javac --release 8 -encoding UTF-8 -d "$OUT" "${sources[@]}" "${tests[@]}"

for test in "${tests[@]}"; do
  name="${test##*/}"
  java -cp "$OUT" "io.freemytv.${name%.java}"
done
printf 'JVM regression suites: %s passed; pure helper classes: %s. (Not line/branch or Android UI coverage.)\n' \
  "${#tests[@]}" "${#helpers[@]}"

for smoke in tests/dashboard_integration.py tests/android_identity.py tests/settings_identity.py tests/home_localization.py tests/no_crypto.py tests/input_order.py; do
  python3 "$smoke"
done
