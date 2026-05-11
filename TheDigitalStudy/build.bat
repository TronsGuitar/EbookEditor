@echo off
REM ── Quick build helper for Windows CMD / PowerShell ────────────────────────

SET TASK=%1
IF "%TASK%"=="" SET TASK=assembleDebug

echo =^> Building: %TASK%
docker compose run --rm android ./gradlew %TASK% --no-daemon

echo.
echo =^> APK output (if applicable):
IF EXIST app\build\outputs\apk (
    dir /s /b app\build\outputs\apk\*.apk 2>nul
) ELSE (
    echo No APK found yet.
)
