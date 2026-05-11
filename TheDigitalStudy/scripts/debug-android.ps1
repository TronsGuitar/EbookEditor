$ErrorActionPreference = "Stop"
$APP_ID   = "com.thedigitalstudy.ebookeditor"
$ACTIVITY = ".MainActivity"
$PORT     = 5005

Write-Host "Clearing any existing ADB forward on port $PORT..."
adb forward --remove tcp:$PORT 2>$null

Write-Host "Launching $APP_ID in debug mode (waiting for debugger)..."
adb shell am start -D -n "${APP_ID}/${ACTIVITY}"
if ($LASTEXITCODE -ne 0) {
    Write-Error "Failed to launch app. Is a device or emulator connected? (check: adb devices)"
    exit 1
}

Write-Host "Waiting for process to appear..."
$pid = $null
for ($i = 0; $i -lt 20; $i++) {
    Start-Sleep -Milliseconds 500
    $result = adb shell pidof $APP_ID 2>$null
    if ($result -and $result.Trim() -ne "") {
        $pid = $result.Trim().Split(" ")[0]
        break
    }
}

if (-not $pid) {
    Write-Error "Process '$APP_ID' did not start within 10 seconds."
    exit 1
}

Write-Host "Process PID: $pid"
Write-Host "Forwarding tcp:$PORT -> jdwp:$pid ..."
adb forward tcp:$PORT jdwp:$pid
if ($LASTEXITCODE -ne 0) {
    Write-Error "ADB port forward failed."
    exit 1
}

Write-Host ""
Write-Host "Ready — attach debugger to localhost:$PORT"
