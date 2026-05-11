# ── The Digital Study — Android Build Image ──────────────────────────────────
#
# Includes: JDK 17, Gradle 8.9, Android SDK (API 35, build-tools 35.0.0)
# No JDK installation required on the host machine.
#
# Build the image:   docker compose build
# Build the APK:     docker compose run --rm android assembleDebug
# Run tests:         docker compose run --rm android test
# ─────────────────────────────────────────────────────────────────────────────

FROM eclipse-temurin:17-jdk-jammy

# ── System dependencies ───────────────────────────────────────────────────────
RUN apt-get update && apt-get install -y --no-install-recommends \
        wget \
        unzip \
        git \
    && rm -rf /var/lib/apt/lists/*

# ── Gradle 8.9 (matches gradle-wrapper.properties) ───────────────────────────
ENV GRADLE_VERSION=8.9
RUN wget -q "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" \
        -O /tmp/gradle.zip \
    && unzip -q /tmp/gradle.zip -d /opt \
    && rm /tmp/gradle.zip
ENV PATH=$PATH:/opt/gradle-${GRADLE_VERSION}/bin

# ── Android SDK command-line tools ────────────────────────────────────────────
ENV ANDROID_HOME=/opt/android-sdk
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH=$PATH:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools

RUN mkdir -p "${ANDROID_HOME}/cmdline-tools"

# Pinned to a known-good release; bump ARG to update.
ARG CMDLINE_TOOLS_BUILD=11076708
RUN wget -q \
        "https://dl.google.com/android/repository/commandlinetools-linux-${CMDLINE_TOOLS_BUILD}_latest.zip" \
        -O /tmp/cmdline-tools.zip \
    && unzip -q /tmp/cmdline-tools.zip -d /tmp \
    && mv /tmp/cmdline-tools "${ANDROID_HOME}/cmdline-tools/latest" \
    && rm /tmp/cmdline-tools.zip

# ── Accept licenses & install SDK packages ────────────────────────────────────
RUN yes | sdkmanager --licenses > /dev/null 2>&1 \
    && sdkmanager \
        "platform-tools" \
        "platforms;android-35" \
        "build-tools;35.0.0"

# ── Entrypoint ────────────────────────────────────────────────────────────────
COPY --chmod=755 docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh

WORKDIR /project

ENTRYPOINT ["docker-entrypoint.sh"]
# Default task — override on the command line as needed
CMD ["./gradlew", "assembleDebug"]
