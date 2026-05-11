#!/usr/bin/env bash
# ── Docker entrypoint for The Digital Study Android builds ───────────────────
set -euo pipefail

# 1. Write local.properties so AGP can locate the SDK.
#    Overwrites any host-machine path that may have been volume-mounted in.
echo "sdk.dir=${ANDROID_HOME}" > /project/local.properties

# 2. Generate the Gradle wrapper JAR if it is missing.
#    The JAR is a binary and cannot be committed without a local JDK.
#    We use the system-installed Gradle to bootstrap it once; after that
#    ./gradlew takes over and respects the project's pinned Gradle version.
WRAPPER_JAR="/project/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "${WRAPPER_JAR}" ]; then
    echo "[entrypoint] gradle-wrapper.jar not found — generating with system Gradle ${GRADLE_VERSION}..."
    gradle wrapper --gradle-version="${GRADLE_VERSION}" --distribution-type=bin
    echo "[entrypoint] Wrapper generated."
fi

# 3. Hand off to whatever command was passed (default: ./gradlew assembleDebug)
exec "$@"
