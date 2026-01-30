# Cara Build dan Install APK

## Metode 1: Build APK Manual (Tanpa Android Studio)

### Windows
```cmd
build-apk.bat
```

### Linux/Mac
```bash
chmod +x build-apk.sh
./build-apk.sh
```

Setelah build selesai, file APK ada di:
```
android/app/build/outputs/apk/debug/app-debug.apk
```

## Metode 2: Install Langsung via USB

### Windows
```cmd
build-install.bat
```

Pastikan:
- HP sudah connect via USB
- USB debugging enabled
- Driver HP sudah terinstall

## Metode 3: Manual dengan Gradle

```bash
cd android
./gradlew assembleDebug
```

APK akan ada di: `android/app/build/outputs/apk/debug/app-debug.apk`

## Metode 4: Build Release APK (Production)

### 1. Generate Keystore
```bash
keytool -genkey -v -keystore release-key.keystore -alias extended-screen -keyalg RSA -keysize 2048 -validity 10000
```

### 2. Edit android/app/build.gradle
Tambahkan:
```gradle
android {
    signingConfigs {
        release {
            storeFile file("../../release-key.keystore")
            storePassword "your-password"
            keyAlias "extended-screen"
            keyPassword "your-password"
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### 3. Build Release
```bash
cd android
./gradlew assembleRelease
```

Release APK: `android/app/build/outputs/apk/release/app-release.apk`

## Install APK ke HP

### Cara 1: Via USB dengan ADB
```bash
adb install android/app/build/outputs/apk/debug/app-debug.apk
```

### Cara 2: Copy File
1. Copy file `app-debug.apk` ke HP (via USB/WhatsApp/Email/Cloud)
2. Buka Settings > Security
3. Enable "Install from Unknown Sources"
4. Buka file APK di File Manager
5. Tap "Install"

### Cara 3: Via USB File Transfer
1. Connect HP ke PC via USB
2. Pilih "File Transfer" mode di HP
3. Copy APK ke folder Downloads di HP
4. Buka File Manager di HP
5. Navigate ke Downloads
6. Tap APK file
7. Tap Install

## Download APK Langsung (Setelah Build)

File APK yang sudah di-build bisa langsung di-share:

**Debug APK**: `android/app/build/outputs/apk/debug/app-debug.apk` (~5-10 MB)
**Release APK**: `android/app/build/outputs/apk/release/app-release.apk` (~3-5 MB)

## Troubleshooting

### "Gradle not found"
Install Gradle atau gunakan gradle wrapper:
```bash
cd android
./gradlew    # Linux/Mac
gradlew.bat  # Windows
```

### "SDK not found"
Set ANDROID_HOME:
```bash
# Windows
set ANDROID_HOME=C:\Users\%USERNAME%\AppData\Local\Android\Sdk

# Linux/Mac
export ANDROID_HOME=$HOME/Android/Sdk
```

### "Build failed"
```bash
cd android
./gradlew clean
./gradlew assembleDebug --stacktrace
```

### "Installation blocked"
- Enable "Install from Unknown Sources"
- Atau "Install Unknown Apps" permission untuk File Manager

## Size Optimization

Untuk memperkecil ukuran APK:

1. **Edit build.gradle**:
```gradle
android {
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
        }
    }
    splits {
        abi {
            enable true
            reset()
            include 'armeabi-v7a', 'arm64-v8a'
        }
    }
}
```

2. **Remove unused resources**
3. **Use WebP instead of PNG**
4. **Enable ProGuard**

Ukuran bisa turun dari ~10MB ke ~3-4MB.

---

**Note**: File APK debug hanya untuk testing. Untuk production, gunakan release APK yang sudah di-sign.
