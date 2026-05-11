# ── The Digital Study — build shortcuts ──────────────────────────────────────
.PHONY: build release test clean shell image

## Build the Docker image (run once, or after changing Dockerfile)
image:
	docker compose build

## Debug APK  →  app/build/outputs/apk/debug/app-debug.apk
build:
	docker compose run --rm android ./gradlew assembleDebug --no-daemon

## Release APK (unsigned)  →  app/build/outputs/apk/release/
release:
	docker compose run --rm android ./gradlew assembleRelease --no-daemon

## Unit tests
test:
	docker compose run --rm android ./gradlew test --no-daemon

## Open a shell inside the build container for debugging
shell:
	docker compose run --rm --entrypoint bash android

## Wipe local Gradle caches (forces full re-download next build)
clean:
	docker compose run --rm android ./gradlew clean --no-daemon

## Remove the named Docker volumes (nuclear option — re-downloads everything)
clean-volumes:
	docker compose down -v
