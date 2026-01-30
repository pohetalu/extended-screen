# Build APK di PC Lain - Panduan Singkat

## Jika Anda Punya Laptop/PC Pribadi di Rumah:

### Langkah 1: Copy Project
- Copy seluruh folder `EXTENDED SCREEN` ke USB/OneDrive
- Atau push ke GitHub lalu clone di PC lain

### Langkah 2: Build APK (Pilih salah satu)

#### A. Pakai Android Studio (Termudah)
1. Install Android Studio di PC pribadi
2. Open project folder `android`
3. Build > Build APK
4. Copy APK ke HP

#### B. Pakai Command Line
```bash
# Install Java di PC pribadi
# Download: https://adoptium.net/

cd android
gradlew.bat assembleDebug
```

### Langkah 3: Transfer APK
- APK location: `android\app\build\outputs\apk\debug\app-debug.apk`
- Copy ke HP via USB/email/cloud
- Install di HP

---

## Build Online (GitHub Actions) - TANPA INSTALL APAPUN!

### Setup (Sekali saja):

1. **Buat GitHub Repository**
   - Buka https://github.com/new
   - Create new repository (bisa private)

2. **Upload Code**
   ```bash
   cd "C:\Users\aswin.sandy\Documents\CODING\EXTENDED SCREEN"
   git init
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git remote add origin https://github.com/USERNAME/REPO.git
   git push -u origin main
   ```

3. **Build APK Otomatis**
   - GitHub Actions akan otomatis build
   - Atau manual: GitHub > Actions > Build APK > Run workflow
   - Download APK dari Artifacts

### Keuntungan Build Online:
- ✅ Tidak perlu install apapun di PC kantor
- ✅ Build di cloud (gratis)
- ✅ APK ready download
- ✅ Bisa akses dari mana saja

---

## Portable Java Setup (Tanpa Install)

### 1. Download Portable Java
https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.17%2B10/OpenJDK17U-jdk_x64_windows_hotspot_17.0.17_10.zip

### 2. Extract ke Project
```
EXTENDED SCREEN/
  tools/
    java/
      bin/
        java.exe
        javac.exe
      lib/
      ...
```

### 3. Build APK
```bash
build-portable.bat
```

Portable Java tidak perlu install, cukup extract!

---

## Opsi Tercepat untuk PC Kantor:

### ⭐ REKOMENDASI: Gunakan GitHub Actions

1. Push code ke GitHub (gratis)
2. Actions akan build otomatis
3. Download APK (1-2 menit)

**Tidak perlu install apapun di PC kantor!**

### Cara Push ke GitHub:
```bash
# Di terminal PC kantor
git config --global user.name "Your Name"
git config --global user.email "your@email.com"

cd "EXTENDED SCREEN"
git init
git add .
git commit -m "Android Extended Screen App"

# Create repo di github.com, lalu:
git remote add origin https://github.com/yourusername/extended-screen.git
git push -u origin main
```

Setelah push, check tab "Actions" di GitHub untuk download APK!
