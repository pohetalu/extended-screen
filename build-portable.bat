@echo off
setlocal enabledelayedexpansion
echo ================================================
echo  Android APK Build - Portable Method
echo ================================================
echo.

REM Check if portable Java exists
if not exist "tools\java\bin\java.exe" (
    echo [ERROR] Portable Java not found
    echo.
    echo To fix:
    echo 1. Download: https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.17%%2B10/OpenJDK17U-jdk_x64_windows_hotspot_17.0.17_10.zip
    echo 2. Extract to: %CD%\tools\java\
    echo 3. Run this script again
    echo.
    pause
    exit /b 1
)

echo [OK] Portable Java found
set "JAVA_HOME=%CD%\tools\java"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo.

echo [1/2] Building APK...
cd android

REM Use gradle bin wrapper
if exist "gradle\wrapper\gradle-8.1\bin\gradle.bat" (
    echo Using gradle from: gradle\wrapper\gradle-8.1\bin
    set "GRADLE_HOME=%CD%\gradle\wrapper\gradle-8.1"
    call "!GRADLE_HOME!\bin\gradle.bat" assembleDebug
) else (
    REM Fallback: use gradlew if available
    if exist "gradlew.bat" (
        echo Using gradlew.bat
        call gradlew.bat assembleDebug
    ) else (
        echo [ERROR] Gradle not found
        cd ..
        pause
        exit /b 1
    )
)

set BUILDRESULT=%ERRORLEVEL%
cd ..

if %BUILDRESULT% EQU 0 (
    echo.
    if exist "android\app\build\outputs\apk\debug\app-debug.apk" (
        echo ================================================
        echo  [SUCCESS] APK Built Successfully!
        echo ================================================
        echo.
        for %%A in ("android\app\build\outputs\apk\debug\app-debug.apk") do (
            echo Location: android\app\build\outputs\apk\debug\app-debug.apk
            echo Size: %%~zA bytes
        )
        echo.
        echo Next steps:
        echo 1. Copy APK to your phone
        echo 2. Install the APK
        echo 3. Enable Accessibility Service
        echo 4. Run: cd pc-server
        echo 5. Run: python server.py
        echo.
    ) else (
        echo [WARNING] Build completed but APK not found
    )
) else (
    echo.
    echo ================================================
    echo  [FAILED] Build Error
    echo ================================================
    echo.
    echo See errors above. Common issues:
    echo - Missing Android SDK
    echo - Missing Kotlin compiler
    echo - Gradle cache corrupted
    echo.
    echo Solution: Use Android Studio instead
    echo https://developer.android.com/studio
    echo.
)

pause

