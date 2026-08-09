# Build Cordova Android App with local Gradle configuration
$gradleBin = "C:\Users\leroy\.gradle\wrapper\dists\gradle-8.14.3-all\10utluxaxniiv4wxiphsi49nj\gradle-8.14.3\bin"
$env:PATH = "$gradleBin;$env:PATH"

# Set Android SDK variables if needed
$env:ANDROID_HOME = "C:\Users\leroy\AppData\Local\Android\Sdk"

Write-Host "Gradle bin added to PATH: $gradleBin"
Write-Host "Android Home: $env:ANDROID_HOME"

$scriptDir = $PSScriptRoot

Write-Host "Manually cleaning build directories using long path prefix..."
$buildDirs = @(
    "$scriptDir\cordova_project\platforms\android\build",
    "$scriptDir\cordova_project\platforms\android\app\build",
    "$scriptDir\cordova_project\platforms\android\CordovaLib\build"
)
foreach ($dir in $buildDirs) {
    if (Test-Path $dir) {
        Write-Host "Deleting $dir"
        Remove-Item -Recurse -Force "\\?\$dir" -ErrorAction SilentlyContinue
    }
}

Set-Location -Path "$scriptDir\cordova_project"
Write-Host "Starting Cordova android build..."
npx cordova build android

if ($LASTEXITCODE -eq 0) {
    Write-Host "Build completed successfully!"
    
    $sourceApk = "$scriptDir\cordova_project\platforms\android\app\build\outputs\apk\debug\app-debug.apk"
    $destApk = "$scriptDir\leeprime.apk"
    
    if (Test-Path $sourceApk) {
        Copy-Item -Path $sourceApk -Destination $destApk -Force
        Write-Host "Successfully copied compiled APK to: $destApk"
    } else {
        Write-Host "Warning: Build succeeded but compiled APK was not found at $sourceApk"
    }
} else {
    Write-Host "Error: Cordova build failed with exit code $LASTEXITCODE"
}
