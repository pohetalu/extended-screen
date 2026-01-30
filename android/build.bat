@echo off
setlocal enabledelayedexpansion

if exist "gradlew.bat" (
    echo Running gradle wrapper...
    call gradlew.bat assembleDebug
) else (
    echo Gradle wrapper not found. Please download from GitHub Actions artifacts.
    echo Or run: gradle assembleDebug
)

pause
