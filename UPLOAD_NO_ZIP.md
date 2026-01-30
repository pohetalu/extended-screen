# Solusi Cepat: Upload ke GitHub Tanpa Zip (Jika File Terlalu Besar)

Karena folder terlalu besar, ada solusi lebih mudah: **Upload file by file langsung di GitHub web!**

## 🚀 Cara Tercepat (Tanpa Compress):

### STEP 1: Buat GitHub Repository

1. Buka https://github.com
2. Login (atau sign up jika belum punya)
3. Klik tombol **"+"** (plus) di kanan atas
4. Pilih **"New repository"**
5. Nama: `extended-screen`
6. Public
7. Klik **"Create repository"**

### STEP 2: Upload Files di GitHub Web

Saat repository dibuat, halaman akan menunjukkan instruksi upload.

Cari tombol **"uploading an existing file"** atau **"Add file"** → **"Upload files"**

Klik tombol tersebut.

### STEP 3: Upload Important Folders SAJA

Drag & drop HANYA folder-folder penting (tidak perlu upload yang besar):

✅ **Upload THESE (Penting):**
- `android/app/src/` (source code) - ~1 MB
- `android/build.gradle` - 1 KB
- `android/settings.gradle` - 1 KB
- `android/gradle.properties` - 1 KB
- `android/app/build.gradle` - 1 KB
- `android/gradle/wrapper/*.properties` - 1 KB
- `android/app/proguard-rules.pro` - 1 KB
- `android/app/src/main/AndroidManifest.xml` - 2 KB
- `android/app/src/main/res/` (resources) - ~2 MB
- `.github/workflows/` (GitHub Actions) - ~1 KB
- `pc-server/` (semua files) - ~50 KB
- `*.md` (README.md, SETUP.md, dll) - ~50 KB
- `.gitignore` - 1 KB

❌ **JANGAN Upload (Terlalu Besar):**
- ~~`tools/java/`~~ (JDK - 100+ MB)
- ~~`android/.gradle/`~~ (cache)
- ~~`android/gradle/wrapper/gradle-8.1/`~~ (Gradle dist - 200+ MB)
- ~~`android/app/build/`~~ (build output)
- ~~`android/.idea/`~~ (IDE files)

### STEP 4: Commit Upload

Setelah upload selesai:
1. Scroll ke bawah
2. Ketik message: `Initial commit - Extended Screen Project`
3. Klik **"Commit changes"**

GitHub akan otomatis upload files dan trigger Actions.

### STEP 5: GitHub Actions Auto-Build

1. Tab **"Actions"**
2. Lihat workflow "Build APK"
3. Status akan berubah jadi hijau ✓ (2-5 menit)
4. Download APK dari Artifacts

---

## 📋 File-File Prioritas

Jika upload lambat, upload ONLY ini dulu:

**MINIMUM untuk GitHub Actions Build (hanya ~3-4 MB):**
```
.github/workflows/build-apk.yml      (CRITICAL!)
android/app/src/main/                (source code)
android/build.gradle
android/settings.gradle
android/gradle.properties
android/app/build.gradle
android/gradle/wrapper/*.properties
pc-server/                           (optional, bisa later)
README.md
.gitignore
```

Ini cukup untuk GitHub Actions build APK!

Folder besar seperti `tools/` dan `gradle-8.1/` tidak perlu upload karena:
- GitHub Actions punya JDK sendiri
- Gradle akan auto-download dependency
- Git ignore rules akan skip anyway

---

## 🎯 Recommended Upload Order:

1. `.github/workflows/build-apk.yml` - **FIRST!** (ini yang penting)
2. `android/app/src/main/` - Source code
3. `android/*.gradle` dan `gradle/wrapper/*.properties` - Build config
4. `android/app/src/main/res/` - Resources
5. `pc-server/` - Server files (optional)
6. `.github/` - GitHub config
7. `*.md` files - Documentation

---

## 💡 Tips GitHub Web Upload:

**Membuat Folder Saat Upload:**
- Saat upload, ketik path dengan `/`
- Contoh: `android/app/src/main/java/com/extendedscreen/android/MainActivity.kt`
- GitHub otomatis buat folder structure

**Upload Multiple Files:**
- Bisa drag & drop multiple files sekaligus
- GitHub akan handle sisanya

**View Repository Structure:**
- Setelah commit, klik folder untuk browse
- Verify semua files ter-upload

---

##  ⏱️ Expected Timeline:

| Langkah | Waktu |
|---------|-------|
| Create repository | 1 menit |
| Upload files (web drag&drop) | 5-10 menit |
| GitHub Actions build | 3-5 menit |
| Download APK | 2 menit |
| **Total** | **~15 menit** |

---

## 🔄 Jika Upload Timeout:

GitHub web ada timeout ~2 jam untuk upload. Jika timeout:

1. Refresh halaman
2. Upload sisa files yang belum
3. Commit
4. GitHub Actions akan rebuild

---

## ✅ Verification Checklist:

Setelah commit, verify:

- [ ] Repository page menampilkan semua files
- [ ] Folder `android/app/src/` ada
- [ ] File `.github/workflows/build-apk.yml` ada
- [ ] Tab "Actions" menampilkan workflow running
- [ ] Status berubah jadi green ✓
- [ ] Artifacts ada (app-debug.zip)

Jika semua centang, APK siap download! 🎉

---

## 📞 Jika Ada Masalah:

**Upload Slow?**
- Normal buat folder besar
- Coba upload di folder kosong dulu untuk test

**Build Gagal di GitHub Actions?**
- Lihat tab Actions untuk error log
- Common issue: file `.github/workflows/build-apk.yml` tidak ada
- Make sure file ini ter-upload dulu

**APK Tidak Ada di Artifacts?**
- Check build status (harus green)
- Scroll down di Actions page
- Artifacts hanya muncul setelah build selesai

---

## Next Steps:

1. Buat GitHub account & repository
2. Upload files penting (terutama `.github/workflows/`)
3. Lihat tab Actions untuk progress
4. Download APK
5. Install ke HP
6. Enjoy! 🎉

**Cara ini 100% bisa tanpa install apapun!**
