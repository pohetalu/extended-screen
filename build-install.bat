@echo off
echo ====================================
echo Building Android APK and Installing
echo ====================================
echo.

echo Make sure phone is connected via USB!
echo.
pause

cd android

echo Cleaning previous builds...
call gradlew clean

echo.
echo Building and installing APK...
call gradlew installDebug

echo.
echo ====================================
echo Installation Complete!
echo ====================================
echo.
pause
