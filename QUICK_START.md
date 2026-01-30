# ⚡ Quick Start - 3 Langkah

## Step 1: Upload ke GitHub

1. Buka GitHub → Create repository `extended-screen`
2. Upload folder `android`, `pc-server`, `.github`
3. Commit & push

## Step 2: Build APK (Automatic)

1. Go to repository → **Actions** tab
2. Workflow `Build APK` running (tunggu 3-5 menit)
3. Status hijau ✓ = Build selesai
4. Download `app-debug.apk` dari Artifacts

## Step 3: Install & Run

**HP:**
1. Transfer APK ke HP
2. Install APK
3. Open app → Tap "Enable Accessibility Service"
4. Connect USB cable ke PC
5. Tap "Start USB Service"

**PC (Terminal):**
```bash
cd pc-server
pip install -r requirements.txt
python server.py
```

**Done!** Move mouse di PC → pointer bergerak di HP 🎉
