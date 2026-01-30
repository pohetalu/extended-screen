@echo off
setlocal enabledelayedexpansion

echo Creating minimal ZIP for GitHub upload...
echo (Only essential files, ~10-20 MB expected)
echo.

REM Create temp folder
if exist temp_upload rmdir /s /q temp_upload
mkdir temp_upload

REM Copy only essential files
echo Copying files...

REM Android source code
xcopy /E /I android\app\src temp_upload\android\app\src 1>nul 2>&1
xcopy /E /I android\gradle\wrapper\*.properties temp_upload\android\gradle\wrapper\ 1>nul 2>&1
copy android\*.gradle temp_upload\android\ 1>nul 2>&1
copy android\settings.gradle temp_upload\android\ 1>nul 2>&1
copy android\gradlew.bat temp_upload\android\ 1>nul 2>&1
copy android\app\proguard-rules.pro temp_upload\android\app\ 1>nul 2>&1

REM .github folder (GitHub Actions)
xcopy /E /I .github temp_upload\.github 1>nul 2>&1

REM PC Server
xcopy /E /I pc-server temp_upload\pc-server 1>nul 2>&1

REM Documentation
copy *.md temp_upload\ 1>nul 2>&1
copy .gitignore temp_upload\ 1>nul 2>&1

echo.
echo Creating ZIP...
cd temp_upload

REM List files for user
dir /s /b | find ".java" | find /c "java"
for /f "tokens=1" %%a in ('dir /s /b ^| find ".java" ^| find /c "java"') do echo Found %%a Kotlin files

REM Create ZIP
cd ..
powershell -Command "Compress-Archive -Path 'temp_upload' -DestinationPath 'extended-screen-upload.zip' -Force; $size = (Get-Item 'extended-screen-upload.zip').Length; if ($size -gt 0) { Write-Host ('ZIP created successfully: ' + [math]::Round($size/1MB, 2) + ' MB') } else { Write-Host 'ZIP creation failed' }"

echo.
echo File ready: extended-screen-upload.zip
echo Location: C:\Users\aswin.sandy\Documents\CODING\
echo.
echo Next steps:
echo 1. Go to GitHub.com
echo 2. Create new repository
echo 3. Upload this ZIP file
echo.

REM Cleanup
rmdir /s /q temp_upload

pause
