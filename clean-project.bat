@echo off
echo ================================================
echo  Clean Project - Remove Unnecessary Files
echo ================================================
echo.
echo Removing large files/folders that don't need upload...
echo.

REM Remove gradle cache
if exist "android\.gradle" (
    echo [DELETE] android\.gradle (cache)
    rmdir /s /q "android\.gradle"
)

REM Remove gradle wrapper distribution
if exist "android\gradle\wrapper\gradle-8.1" (
    echo [DELETE] android\gradle\wrapper\gradle-8.1 (dist)
    rmdir /s /q "android\gradle\wrapper\gradle-8.1"
)

REM Remove gradle zip
if exist "android\gradle\wrapper\gradle-8.1-bin.zip" (
    echo [DELETE] android\gradle\wrapper\gradle-8.1-bin.zip
    del "android\gradle\wrapper\gradle-8.1-bin.zip"
)

REM Remove build outputs
if exist "android\app\build" (
    echo [DELETE] android\app\build (output)
    rmdir /s /q "android\app\build"
)

REM Remove IDE files
if exist "android\.idea" (
    echo [DELETE] android\.idea (IDE)
    rmdir /s /q "android\.idea"
)

if exist "android\.iml" (
    del "android\.iml"
)

REM Remove local properties
if exist "android\local.properties" (
    echo [DELETE] android\local.properties
    del "android\local.properties"
)

REM Remove portable Java (VERY BIG!)
if exist "tools\java" (
    echo [DELETE] tools\java (portable JDK - VERY BIG!)
    rmdir /s /q "tools\java"
)

REM Remove gradle wrapper jar zip
if exist "android\gradle\wrapper\gradle-wrapper.jar" (
    echo [DELETE] android\gradle\wrapper\gradle-wrapper.jar
    del "android\gradle\wrapper\gradle-wrapper.jar"
)

echo.
echo ================================================
echo  Cleanup Complete!
echo ================================================
echo.
echo Remaining important files:
echo - android/app/src/main/java/ (source code)
echo - android/app/build.gradle (build config)
echo - android/settings.gradle
echo - android/gradlew.bat (gradle wrapper script)
echo - pc-server/ (server code)
echo - .github/workflows/ (GitHub Actions)
echo - README.md, SETUP.md
echo.
echo Folder size should be ~10-20 MB now
echo.
echo Next: Zip folder dan upload ke GitHub
echo.
pause
