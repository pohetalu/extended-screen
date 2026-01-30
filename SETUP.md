# Extended Screen - Setup Guide

## Prasyarat

### Android
- HP dengan Android 7.0 (Nougat) atau lebih tinggi
- USB cable dengan data transfer support
- USB debugging enabled

### PC
- Windows/Linux/MacOS
- Python 3.7 atau lebih baru
- Android Studio (untuk build Android app)

## Setup Langkah demi Langkah

### Bagian 1: Build Android App

1. **Install Android Studio**
   - Download dari https://developer.android.com/studio
   - Install dengan default settings

2. **Open Project**
   - Buka Android Studio
   - File > Open > Pilih folder `android`
   - Wait for Gradle sync selesai

3. **Enable USB Debugging di HP**
   - Buka Settings
   - About Phone > Tap "Build Number" 7x (untuk enable Developer Options)
   - Developer Options > Enable "USB Debugging"

4. **Build & Install**
   - Connect HP ke PC via USB
   - Allow USB debugging saat popup muncul
   - Click tombol Run (▶️) di Android Studio
   - Pilih device Anda
   - Wait until app installed

### Bagian 2: Setup PC Server

1. **Install Python**
   - Download dari https://www.python.org/downloads/
   - Install dengan checkbox "Add Python to PATH"

2. **Install Dependencies**
   ```bash
   cd pc-server
   pip install -r requirements.txt
   ```

3. **Install libusb (Windows)**
   - Download Zadig dari https://zadig.akeo.ie/
   - Jalankan Zadig
   - Options > List All Devices
   - Pilih Android device Anda
   - Install WinUSB driver

4. **Test Connection**
   ```bash
   python -c "import usb.core; print(usb.core.find())"
   ```
   Seharusnya menampilkan device info

### Bagian 3: Konfigurasi

#### Android App

1. Buka aplikasi di HP
2. Tap "Enable Accessibility Service"
3. Cari "Extended Screen" di list
4. Enable toggle
5. Tap "Allow" saat popup muncul
6. Kembali ke app

#### PC Server

Edit `server.py` jika diperlukan:

```python
# Line 88-89: Sesuaikan dengan resolusi monitor Anda
self.screen_width = 1920
self.screen_height = 1080

# Line 32: Sesuaikan Vendor ID jika HP tidak terdeteksi
# Google Pixel: 0x18d1
# Samsung: 0x04e8
# Xiaomi: 0x2717
# dll.
self.device = usb.core.find(idVendor=0x18d1)
```

### Bagian 4: Testing

1. **Start Android Service**
   - Buka app di HP
   - Tap "Start USB Service"
   - Status should show "Connected via USB"

2. **Start PC Server**
   ```bash
   cd pc-server
   python server.py
   ```
   Output should show:
   ```
   Looking for Android device...
   Connected to device: [Manufacturer] [Model]
   Starting input capture...
   Input capture started!
   ```

3. **Test Input**
   - Gerakkan mouse → pointer bergerak di HP
   - Klik mouse → tap di HP
   - Ketik di keyboard → input di HP
   - Scroll mouse → scroll di HP

## Troubleshooting

### USB Device Not Found

**Windows:**
```bash
# Check if device detected
python -c "import usb.core; devices = list(usb.core.find(find_all=True)); print(f'Found {len(devices)} devices'); [print(f'  {d}') for d in devices]"
```

**Linux:**
```bash
# Add udev rules
sudo nano /etc/udev/rules.d/51-android.rules

# Add this line (replace XXXX with vendor ID):
SUBSYSTEM=="usb", ATTR{idVendor}=="18d1", MODE="0666", GROUP="plugdev"

# Reload rules
sudo udevadm control --reload-rules
sudo udevadm trigger
```

### Accessibility Service Not Working

1. Force stop app
2. Go to Settings > Accessibility
3. Disable "Extended Screen"
4. Re-enable it
5. Restart app

### Permission Denied (Linux)

```bash
# Add user to plugdev group
sudo usermod -a -G plugdev $USER

# Logout and login again
```

### Mouse/Keyboard Not Captured (Linux)

```bash
# Run with sudo (not recommended for security)
sudo python server.py

# OR add user to input group
sudo usermod -a -G input $USER
```

## Advanced Configuration

### Custom Key Bindings

Edit `server.py`, method `on_key_press()`:

```python
def on_key_press(self, key):
    # Ignore certain keys
    if key == keyboard.Key.esc:
        return  # Don't send ESC to phone
    
    # Custom shortcuts
    if key == keyboard.Key.f12:
        # Toggle capture on/off
        self.toggle_capture()
        return
    
    # ... rest of code
```

### Screen Regions

Limit mouse control to certain region:

```python
def on_mouse_move(self, x, y):
    # Only send if mouse in right half of screen
    if x < self.screen_width / 2:
        return
    
    # Map right half to full phone screen
    norm_x = (x - self.screen_width / 2) / (self.screen_width / 2)
    norm_y = y / self.screen_height
    
    # ... rest of code
```

### Multiple Devices

```python
# Find specific device by serial number
device = usb.core.find(idVendor=0x18d1, 
                       custom_match=lambda d: d.serial_number == "ABC123")
```

## Performance Tuning

### Reduce Latency

1. **Android App** - Reduce notification updates
2. **PC Server** - Increase USB transfer rate
3. **Use USB 3.0** cable and port
4. **Disable battery optimization** for app

### Reduce CPU Usage

```python
# Add throttling to mouse move events
last_move_time = 0

def on_mouse_move(self, x, y):
    current_time = time.time()
    if current_time - last_move_time < 0.016:  # 60 FPS
        return
    last_move_time = current_time
    # ... rest of code
```

## Security Best Practices

1. Only use on trusted PC
2. Disable app when not in use
3. Don't grant accessibility service to unknown apps
4. Review app permissions regularly

## Backup & Restore

### Export Settings
```bash
# Backup app data (requires root)
adb backup -f backup.ab com.extendedscreen.android
```

### Restore Settings
```bash
adb restore backup.ab
```

---

**Need Help?** Check README.md atau buat issue di repository.
