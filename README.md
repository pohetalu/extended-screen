# Android Extended Screen

Aplikasi Android yang memungkinkan kontrol HP dari PC melalui kabel USB menggunakan mouse dan keyboard. HP akan berfungsi sebagai extended screen yang dapat dikontrol dari PC.

## Fitur

- 🖱️ Kontrol pointer dengan mouse PC
- ⌨️ Input teks dengan keyboard PC
- 📜 Scroll menggunakan mouse wheel
- 🔌 Koneksi via USB (lebih stabil dari WiFi)
- 🎯 Touch event injection menggunakan Accessibility Service

## Struktur Project

```
EXTENDED SCREEN/
├── android/                    # Aplikasi Android
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/com/extendedscreen/android/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   └── service/
│   │   │   │       ├── UsbControlService.kt
│   │   │   │       └── AccessibilityControlService.kt
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle
│   ├── build.gradle
│   └── settings.gradle
└── pc-server/                  # Server PC (Python)
    ├── server.py
    ├── requirements.txt
    ├── install.bat
    └── run.bat
```

## Instalasi

### Aplikasi Android

1. Buka project Android di Android Studio
2. Connect HP via USB dan enable USB debugging
3. Build dan install aplikasi:
   ```bash
   cd android
   ./gradlew installDebug
   ```

### PC Server

1. Install Python 3.7 atau lebih baru
2. Install dependencies:
   ```bash
   cd pc-server
   install.bat
   ```
   Atau manual:
   ```bash
   pip install -r requirements.txt
   ```

## Cara Penggunaan

### 1. Setup Android

1. Install dan buka aplikasi di HP
2. Tap "Enable Accessibility Service"
3. Aktifkan "Extended Screen" accessibility service
4. Kembali ke aplikasi dan tap "Start USB Service"

### 2. Setup PC

1. Hubungkan HP ke PC via USB
2. Pastikan USB debugging enabled
3. Jalankan server PC:
   ```bash
   cd pc-server
   run.bat
   ```
   Atau manual:
   ```bash
   python server.py
   ```

### 3. Mulai Menggunakan

- Gerakkan mouse untuk mengontrol pointer di HP
- Klik mouse untuk tap di HP
- Ketik di keyboard untuk input teks
- Scroll mouse wheel untuk scroll di HP

## Konfigurasi

### Android App

Edit [MainActivity.kt](android/app/src/main/java/com/extendedscreen/android/MainActivity.kt) untuk kustomisasi UI.

### PC Server

Edit [server.py](pc-server/server.py) untuk menyesuaikan:
- Resolusi layar PC (line 88-89)
- USB vendor ID/product ID (line 32)

## Cara Kerja

1. **PC Server** menangkap event mouse dan keyboard menggunakan `pynput`
2. Event di-encode ke dalam protokol binary
3. Data dikirim ke HP via USB menggunakan `pyusb`
4. **UsbControlService** di Android menerima data via USB
5. Command di-broadcast ke **AccessibilityControlService**
6. Accessibility service melakukan touch injection dan input teks

## Protokol Komunikasi

| Command | Byte | Payload |
|---------|------|---------|
| Mouse Move | 0x01 | float x, float y (normalized 0-1) |
| Mouse Click | 0x02 | byte button (1=left, 2=right, 3=middle) |
| Keyboard | 0x03 | UTF-8 string |
| Scroll | 0x04 | float delta |

## Troubleshooting

### Android tidak terdeteksi di PC

- Pastikan USB debugging enabled
- Install USB drivers untuk HP Anda
- Coba port USB lain
- Izinkan USB debugging saat popup muncul di HP

### Accessibility Service tidak berfungsi

- Restart aplikasi setelah enable accessibility service
- Pastikan permission diberikan di Settings > Accessibility
- Beberapa HP memerlukan restart untuk aktivasi

### Input tidak bekerja

- Pastikan kedua aplikasi running (Android + PC)
- Check log di Android Logcat
- Pastikan tidak ada aplikasi lain yang menggunakan USB

## Keamanan

- USB communication tidak encrypted
- Hanya untuk penggunaan pribadi
- Jangan gunakan di jaringan tidak aman
- Accessibility service memiliki akses penuh ke HP

## Pengembangan Lebih Lanjut

### Fitur yang bisa ditambahkan:

- [ ] Custom cursor overlay di Android
- [ ] Clipboard sync antara PC dan Android
- [ ] Multi-touch gesture support
- [ ] Screen mirroring ke PC
- [ ] Wireless mode (WiFi/Bluetooth)
- [ ] Hotkey configuration
- [ ] Multi-device support

### Kontribusi

Silakan buat pull request atau issue untuk improvement.

## Lisensi

MIT License - Bebas digunakan untuk keperluan pribadi dan komersial.

## Credits

Dibuat dengan:
- Kotlin untuk Android app
- Python untuk PC server
- pynput untuk input capture
- pyusb untuk USB communication

---

**Note:** Aplikasi ini memerlukan:
- Android 7.0 (API 24) atau lebih tinggi
- Python 3.7+ di PC
- USB cable dengan data transfer support
