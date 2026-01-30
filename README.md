# Extended Screen - PC to Android Control

Aplikasi untuk mengontrol HP Android dari PC via USB cable menggunakan mouse dan keyboard sebagai extended screen.

## Fitur Utama

- 🖱️ **Mouse Control** - Kontrol pointer dan click dari PC ke HP
- ⌨️ **Keyboard Input** - Ketik di PC, input di HP
- 🔄 **Real-time** - Responsif tanpa delay signifikan
- 📱 **USB Cable Connection** - Stabil dan aman
- ✨ **Minimal Setup** - Install & jalankan

## Struktur Project

```
EXTENDED SCREEN/
├── android/                    # Android app (Kotlin)
│   ├── app/src/main/
│   │   ├── java/              # Kotlin source files
│   │   └── res/               # Resources
│   ├── build.gradle           # App build config
│   ├── settings.gradle        # Project settings
│   ├── gradle.properties      # Gradle properties
│   └── gradlew.bat            # Gradle wrapper (Windows)
├── pc-server/                  # PC server (Python)
│   ├── server.py              # Main server script
│   ├── requirements.txt       # Dependencies
│   ├── install.bat            # Install dependencies
│   └── run.bat                # Run server
├── .github/workflows/         # GitHub Actions
│   └── build-apk.yml         # Auto-build workflow
└── README.md                  # This file
```

## Quick Start

### 1. Build APK dengan GitHub Actions (Recommended)

**Untuk PC Kantor (No Install):**

1. Extract folder project
2. Upload ke GitHub repository baru
3. GitHub Actions otomatis build APK
4. Download APK dari Artifacts

### 2. Install di HP

1. Transfer APK ke HP
2. Buka APK, install
3. Buka app, tap "Enable Accessibility Service"
4. Connect HP ke PC via USB cable
5. Tap "Start USB Service"

### 3. Jalankan Server di PC

```bash
cd pc-server
pip install -r requirements.txt
python server.py
```

## Spesifikasi Teknis

### Android App (Kotlin)
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 15)
- **Build Tool:** Gradle 8.1
- **Language:** Kotlin 1.9.0

**Components:**
- `MainActivity.kt` - UI entry point
- `UsbControlService.kt` - USB communication service
- `AccessibilityControlService.kt` - Touch/keyboard injection service

### PC Server (Python)
- **Dependencies:** pynput, pyusb
- **Input:** Mouse & keyboard capture
- **Output:** USB data transmission

### USB Protocol
- Command types: MOUSE_MOVE, MOUSE_CLICK, KEYBOARD, SCROLL
- Binary protocol dengan struct.pack encoding
- Normalized coordinates (0.0-1.0 range)

## Setup Panduan

### Untuk Build di PC Pribadi

Jika ingin compile manual (optional):

```bash
cd android
./gradlew.bat build
```

Hasil APK: `android/app/build/outputs/apk/debug/app-debug.apk`

### Untuk PC Kantor (No Installation)

Gunakan GitHub Actions (built-in workflow):
1. Upload project ke GitHub
2. Actions tab → workflow run otomatis
3. Download APK

## Troubleshooting

**Problem: APK tidak terinstall**
- Pastikan "Install from unknown sources" enabled
- Cek storage HP memiliki ruang cukup

**Problem: Accessibility Service error**
- Buka Settings → Accessibility
- Tap "Extended Screen"
- Enable service

**Problem: USB connection failed**
- Cek USB cable
- Enable "USB Debugging" di HP

**Problem: Server tidak terdeteksi device**
- Pastikan USB cable connected
- HP sudah menjalankan USB Service
- Coba disconnect & reconnect USB

## Protokol Komunikasi

Binary protocol via USB:

```
MOUSE_MOVE (0x01):
  [1 byte: 0x01] [4 bytes: x (float)] [4 bytes: y (float)]

MOUSE_CLICK (0x02):
  [1 byte: 0x02] [1 byte: button_id]
  button_id: 1=left, 2=right, 3=middle

KEYBOARD (0x03):
  [1 byte: 0x03] [4 bytes: string_length] [n bytes: UTF-8 string]

SCROLL (0x04):
  [1 byte: 0x04] [4 bytes: delta (float)]
```

## License

MIT License - Bebas digunakan untuk personal & komersial

## Support

Untuk issue atau pertanyaan, buat issue di GitHub repository.
