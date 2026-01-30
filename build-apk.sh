#!/bin/bash

echo "===================================="
echo "Building Android APK"
echo "===================================="
echo

cd android

echo "Cleaning previous builds..."
./gradlew clean

echo
echo "Building debug APK..."
./gradlew assembleDebug

echo
echo "===================================="
echo "Build Complete!"
echo "===================================="
echo
echo "APK Location:"
echo "android/app/build/outputs/apk/debug/app-debug.apk"
echo
echo "To install:"
echo "1. Copy APK to phone via USB/Cloud"
echo "2. Enable 'Install from Unknown Sources'"
echo "3. Open APK file on phone to install"
echo
