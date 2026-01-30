# GitHub Actions Build APK - Panduan Lengkap untuk Pemula

Panduan ini untuk Anda yang baru pertama kali menggunakan GitHub. Semua langkah dijelaskan detail!

---

## 📋 Ringkasan Singkat

Yang akan kita lakukan:
1. Buat akun GitHub gratis (2 menit)
2. Buat repository baru (1 menit)
3. Upload project code Anda (5 menit)
4. GitHub otomatis build APK di cloud (2-3 menit)
5. Download APK ke HP Anda (1 menit)

**Total waktu: ~15 menit pertama kali**

Setelah itu, setiap kali push code, APK auto-build dalam 2-3 menit!

---

## 🔧 Persiapan

Pastikan Anda punya:
- ✅ Browser internet (Chrome, Firefox, Edge, dll)
- ✅ Email aktif (untuk GitHub account)
- ✅ Folder "EXTENDED SCREEN" siap di PC

---

## STEP 1: Buat GitHub Account

### 1.1 Buka Website GitHub

Buka browser, ketik atau copy-paste link ini:
```
https://github.com
```

Tekan Enter. Anda akan lihat halaman GitHub home.

### 1.2 Klik "Sign Up"

Di halaman GitHub, cari tombol **"Sign up"** (biasanya di bagian atas kanan).

Klik tombol tersebut.

### 1.3 Isi Data

Halaman akan meminta data:

**Email Address:**
- Ketik email Anda yang aktif
- Contoh: `nama@gmail.com` atau `nama@outlook.com`

**Password:**
- Buat password yang kuat (minimal 15 karakter)
- Contoh: `MyPassword2026GitHub!`
- ⚠️ Ingat password ini!

**Username:**
- Ini nama akun GitHub Anda
- Tidak perlu sama dengan nama asli
- Contoh: `aswin-dev`, `extended-screen-dev`, dll
- Harus unik (tidak ada orang lain yang punya)

Setelah isi semua, klik **"Create account"**.

### 1.4 Verifikasi Email

GitHub akan kirim email ke Anda. Buka email Anda dan:
1. Cari email dari GitHub (di inbox atau spam)
2. Klik link untuk verify
3. Selesai!

Sekarang Anda punya akun GitHub ✓

---

## STEP 2: Buat Repository Baru

### 2.1 Login ke GitHub

Setelah verify email, Anda sudah login otomatis.

Di halaman GitHub, klik di tombol **"+"** (plus) di bagian atas kanan, lalu pilih **"New repository"**.

Atau bisa langsung ke: https://github.com/new

### 2.2 Isi Repository Details

Halaman akan membuka form untuk membuat repository baru. Isi seperti ini:

**Repository name:**
```
extended-screen
```
(atau nama lain sesuka Anda, tidak ada spasi)

**Description (opsional):**
```
Android Extended Screen - Control phone from PC via USB
```

**Public atau Private?**
Pilih: **Public** (agar lebih mudah, nanti bisa private)

**Initialize this repository with:**
Jangan dicentang apapun (skip ini)

Scroll ke bawah, klik tombol **"Create repository"** (berwarna hijau).

### 2.3 Repository Sudah Dibuat!

Selamat! Anda sudah punya repository kosong di GitHub.

Sekarang tampilan akan menunjukkan repository kosong dengan instruksi.

Jangan tutup halaman ini dulu. Kita butuh di step selanjutnya.

---

## STEP 3: Upload Project Files

Ada 2 cara upload. Saya kasih cara termudah (tidak perlu command line):

### CARA A: Upload via Web Browser (TERMUDAH - Recommended)

#### 3A.1 Buka File Explorer di PC Anda

1. Tekan tombol **Windows + E** pada keyboard
2. Atau buka File Explorer dari Taskbar

Cari folder: `C:\Users\aswin.sandy\Documents\CODING\EXTENDED SCREEN`

#### 3A.2 Zip Semua File

Di dalam folder "EXTENDED SCREEN":
1. Select semua files dan folders (Ctrl + A)
2. Klik kanan pada selection
3. Pilih "Send to" → "Compressed (zipped) folder"
4. Akan dibuat file baru: `EXTENDED SCREEN.zip`

Tunggu sampai ZIP selesai dibuat (bisa 30-60 detik tergantung ukuran).

#### 3A.3 Kembali ke Browser GitHub

Di halaman GitHub repository kosong Anda, cari tombol:
```
"uploading an existing file"
```

Atau klik link besar yang mengatakan "uploading an existing file".

#### 3A.4 Upload ZIP File

1. Akan ada box untuk drag & drop atau browse
2. Klik di area tersebut atau cari tombol "Browse files"
3. Pilih file `EXTENDED SCREEN.zip` yang baru dibuat
4. Tunggu upload selesai

Upload bisa memakan beberapa menit tergantung kecepatan internet.

#### 3A.5 Commit Upload

Setelah upload selesai:
1. Di bagian bawah halaman, ada field untuk "Commit message"
2. Ketik: `Initial commit - Added Extended Screen project`
3. Klik tombol **"Commit changes"** (hijau)

GitHub akan extract ZIP dan save semua files.

Tunggu beberapa saat sampai selesai.

---

### CARA B: Upload File by File (Jika ZIP Terlalu Besar)

Jika ZIP file terlalu besar (>1GB), gunakan cara ini:

#### 3B.1 Di Halaman Repository

Klik tombol **"Add file"** → **"Upload files"**

#### 3B.2 Drag & Drop Files

Halaman akan menunjukkan area drag & drop.

Dari File Explorer PC Anda, drag files-files penting:
- `android/` folder (paling penting)
- `pc-server/` folder
- `README.md`
- `SETUP.md`
- File `.github/workflows/build-apk.yml` (ini yang penting!)

#### 3B.3 Commit

Setelah drag selesai, scroll ke bawah dan klik **"Commit changes"**.

---

## STEP 4: GitHub Actions Otomatis Build

### 4.1 Tunggu Build Dimulai

Setelah Anda commit files, GitHub akan otomatis:
1. Detect file `.github/workflows/build-apk.yml`
2. Memulai build process
3. Compile APK di cloud

Ini terjadi otomatis tanpa Anda perlu apapun!

### 4.2 Monitor Build Progress

1. Di halaman repository GitHub Anda, cari tab **"Actions"**
2. Klik tab tersebut
3. Akan melihat list workflows

Cari item terbaru dengan nama "Build APK" atau task name.

#### Status Build:

- 🟡 **Yellow/Orange** = Sedang running (tunggu)
- 🟢 **Green** = Berhasil!
- 🔴 **Red** = Error (lihat log)

### 4.3 Lihat Build Details

Klik pada workflow item untuk lihat detail:

```
Build APK #1
✓ Set up JDK 17
✓ Grant execute permission for gradlew
✓ Build Debug APK
✓ Upload APK
```

Semua harus hijau (✓).

Proses build biasanya butuh **2-5 menit**. First time bisa sampai 10 menit karena download dependencies.

---

## STEP 5: Download APK

### 5.1 Workflow Sudah Selesai

Setelah build selesai (status hijau), scroll ke bawah di halaman workflow.

Anda akan lihat section: **"Artifacts"**

```
app-debug
```

### 5.2 Klik Download

Klik pada **"app-debug"** untuk download.

File akan download ke folder Downloads Anda dengan nama: `app-debug.zip`

### 5.3 Extract APK

1. Buka file `app-debug.zip`
2. Extract
3. Cari file: `app-debug.apk`

**Ini adalah file APK Anda yang siap diinstall!**

File ini berukuran sekitar 10-20 MB.

---

## STEP 6: Install APK ke HP

### 6.1 Transfer APK ke HP

Ada beberapa cara:

**Cara 1: Via USB Cable**
1. Connect HP ke PC dengan USB
2. Copy file `app-debug.apk` ke HP
3. Buka File Manager di HP
4. Cari file APK
5. Tap file APK
6. Install

**Cara 2: Via Email**
1. Attach file `app-debug.apk` dalam email
2. Send ke email HP Anda
3. Buka email di HP
4. Download attachment
5. Tap file, install

**Cara 3: Via Google Drive**
1. Upload file ke Google Drive
2. Di HP, buka Google Drive
3. Download file
4. Tap file, install

### 6.2 Install

Saat install, HP akan minta permission:
```
Allow installation from unknown sources?
```

Tap **"Install anyway"** atau **"Allow"**.

Tunggu proses install selesai.

**Selesai! App sudah terinstall di HP Anda!** ✓

---

## 📱 Langkah Selanjutnya (Setup App di HP)

### 7.1 Buka Aplikasi di HP

Di HP, cari app "Extended Screen" atau "ExtendedScreen" di app drawer.

Tap untuk buka.

### 7.2 Enable Accessibility Service

1. App akan menampilkan tombol "Enable Accessibility Service"
2. Tap tombol tersebut
3. Akan membuka Settings
4. Cari dan enable "Extended Screen"
5. Tap "Allow" saat diminta permission

### 7.3 Connect USB dan Start Service

1. Connect HP ke PC dengan USB cable
2. Kembali ke app, tap "Start USB Service"
3. Status akan berubah menjadi "Connected via USB"

**HP sekarang ready!** ✓

---

## 💻 Setup PC Server

### 8.1 Buka Terminal di PC

1. Buka Command Prompt atau PowerShell
2. Navigate ke folder pc-server:
```bash
cd "C:\Users\aswin.sandy\Documents\CODING\EXTENDED SCREEN\pc-server"
```

### 8.2 Install Python Dependencies

```bash
pip install -r requirements.txt
```

Tunggu selesai.

### 8.3 Start Server

```bash
python server.py
```

Output akan menampilkan:
```
=== Android Extended Screen - PC Server ===
Looking for Android device...
Connected to device: [Manufacturer] [Model]
Starting input capture...
Input capture started!
```

**Selesai! Server PC sudah running!** ✓

---

## 🎮 Testing

Sekarang Anda bisa:
- ✅ Gerakkan mouse → pointer bergerak di HP
- ✅ Klik mouse → tap di HP
- ✅ Ketik keyboard → input di HP
- ✅ Scroll mouse → scroll di HP

---

## 🔄 Next Time (Kalau Mau Update)

Jika nanti ingin build lagi atau update code:

### Via GitHub Web:
1. Buka repository GitHub Anda
2. Edit files atau upload files baru
3. Commit changes
4. GitHub Actions otomatis build
5. Download APK dari Artifacts

### Atau via Command Line (Git):

Jika sudah familiar:
```bash
cd "EXTENDED SCREEN"
git add .
git commit -m "Update message"
git push
```

GitHub Actions akan auto-build!

---

## 🆘 Troubleshooting

### Build Failed (Red status)

**Kemungkinan Penyebab:**
1. File `.github/workflows/build-apk.yml` missing
2. Android SDK issue
3. Dependency issue

**Solusi:**
1. Check tab "Actions" → klik workflow yang failed
2. Scroll ke log untuk lihat error
3. Baca error message
4. Coba re-run atau upload ulang

### APK Tidak Ada di Artifacts

- Check status build (harus hijau/success dulu)
- Scroll down di halaman workflow
- Kalau truly tidak ada, build gagal (lihat error log)

### File Tidak Terupload

- Pastikan upload complete (progress bar 100%)
- Refresh halaman
- Coba upload lagi

### Internet Timeout

- Pastikan koneksi internet stabil
- Jika timeout, refresh dan retry
- Jika ZIP terlalu besar, gunakan Cara B (file by file)

---

## ✅ Checklist Ringkas

- [ ] Buat GitHub account
- [ ] Buat repository "extended-screen"
- [ ] Upload project files
- [ ] Check tab Actions untuk build status
- [ ] Download APK dari Artifacts
- [ ] Transfer APK ke HP
- [ ] Install APK
- [ ] Enable Accessibility Service
- [ ] Connect USB dan start service
- [ ] Run PC server
- [ ] Test mouse/keyboard control

---

## 💡 Tips & Tricks

1. **Bookmark Repository URL**
   - Save URL repository untuk akses cepat
   - Contoh: `https://github.com/username/extended-screen`

2. **Star Repository**
   - Klik star icon untuk bookmark
   - Akan muncul di profile Anda

3. **Private Repository**
   - Jika privacy concern, ubah ke Private
   - Settings → Change repository visibility

4. **Share dengan Teman**
   - Bisa share repository URL
   - Teman bisa download APK dari Artifacts

5. **Build History**
   - Tab "Actions" menampilkan semua build history
   - Bisa download APK dari build yang berbeda

---

## 📞 Perlu Bantuan?

Jika ada yang tidak jelas, tanyakan di step mana Anda stuck!

Saya siap bantu untuk:
- ✅ Navigasi GitHub web
- ✅ Troubleshoot build errors
- ✅ Download & install APK
- ✅ Setup app di HP
- ✅ Run PC server

---

**Selamat mencoba! Semoga lancar! 🚀**
