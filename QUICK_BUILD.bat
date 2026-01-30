@echo off
echo ================================================
echo  Android Extended Screen - Quick Build
echo ================================================
echo.

REM Check if Java installed
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java not found!
    echo.
    echo Please install Java first:
    echo 1. Download from: https://adoptium.net/
    echo 2. Install JDK 17
    echo 3. Restart terminal
    echo.
    echo OR use Android Studio:
    echo https://developer.android.com/studio
    echo.
    pause
    exit /b 1
)

echo [1/3] Checking Java version...
java -version
echo.

echo [2/3] Building APK...
cd android
call gradlew.bat assembleDebug
cd ..

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ================================================
    echo  BUILD SUCCESS!
    echo ================================================
    echo.
    echo APK location:
    echo   android\app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo Next steps:
    echo 1. Copy APK to phone
    echo 2. Install on phone
    echo 3. Enable Accessibility Service
    echo 4. Run: cd pc-server
    echo 5. Run: python server.py
    echo.
) else (
    echo.
    echo ================================================
    echo  BUILD FAILED!
    echo ================================================
    echo.
    echo Try using Android Studio instead:
    echo https://developer.android.com/studio
    echo.
)

pause
