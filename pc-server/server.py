#!/usr/bin/env python3
"""
Extended Screen PC Server
Captures mouse and keyboard input from PC and sends to Android device via USB
"""

import struct
import sys
import time
from pynput import mouse, keyboard
from pynput.mouse import Controller as MouseController, Listener as MouseListener
from pynput.keyboard import Listener as KeyboardListener

try:
    import usb.core
    import usb.util
except ImportError:
    print("Error: pyusb not installed")
    print("Install with: pip install pyusb")
    sys.exit(1)

# Command types (must match Android app)
MOUSE_MOVE = 0x01
MOUSE_CLICK = 0x02
KEYBOARD = 0x03
SCROLL = 0x04

class InputCapture:
    """Captures mouse and keyboard input from PC"""
    
    def __init__(self, on_command):
        self.on_command = on_command
        self.mouse_controller = MouseController()
        self.mouse_listener = MouseListener(
            on_move=self.on_mouse_move,
            on_click=self.on_mouse_click,
            on_scroll=self.on_scroll
        )
        self.keyboard_listener = KeyboardListener(
            on_press=None,
            on_release=self.on_key_release
        )
        self.last_x = 0
        self.last_y = 0
        self.screen_width = 1920
        self.screen_height = 1080

    def start(self):
        """Start capturing input"""
        print("Starting input capture...")
        self.mouse_listener.start()
        self.keyboard_listener.start()
        print("Input capture started")

    def stop(self):
        """Stop capturing input"""
        self.mouse_listener.stop()
        self.keyboard_listener.stop()

    def on_mouse_move(self, x, y):
        """Handle mouse movement"""
        if abs(x - self.last_x) > 5 or abs(y - self.last_y) > 5:
            norm_x = x / self.screen_width
            norm_y = y / self.screen_height
            self.on_command(MOUSE_MOVE, norm_x, norm_y)
            self.last_x = x
            self.last_y = y

    def on_mouse_click(self, x, y, button, pressed):
        """Handle mouse click"""
        if pressed:
            button_id = 1 if button == mouse.Button.left else 2 if button == mouse.Button.right else 3
            norm_x = x / self.screen_width
            norm_y = y / self.screen_height
            self.on_command(MOUSE_CLICK, norm_x, norm_y, button_id)

    def on_scroll(self, x, y, dx, dy):
        """Handle scroll"""
        self.on_command(SCROLL, dy)

    def on_key_release(self, key):
        """Handle keyboard input"""
        try:
            if hasattr(key, 'char') and key.char:
                self.on_command(KEYBOARD, key.char)
        except:
            pass


class UsbController:
    """Controls USB communication with Android device"""
    
    def __init__(self):
        self.device = None
        self.ep_out = None
        self.ep_in = None
        self.connected = False

    def connect(self):
        """Connect to USB device"""
        print("Searching for USB device...")
        
        # Try to find Android device
        devices = usb.core.find(find_all=True)
        
        for device in devices:
            try:
                if device.idVendor == 0x18d1 or device.idVendor == 0x0bb4:  # Google/HTC
                    print(f"Found device: {device.idVendor:04x}:{device.idProduct:04x}")
                    
                    if device.is_kernel_driver_active(0):
                        device.detach_kernel_driver(0)
                    
                    device.set_configuration()
                    
                    # Find endpoints
                    cfg = device.get_active_configuration()
                    intf = cfg[(0, 0)]
                    
                    self.ep_out = usb.util.find_descriptor(
                        intf,
                        custom_match=lambda e: usb.util.endpoint_direction(e.bEndpointAddress) == usb.util.ENDPOINT_OUT
                    )
                    
                    self.ep_in = usb.util.find_descriptor(
                        intf,
                        custom_match=lambda e: usb.util.endpoint_direction(e.bEndpointAddress) == usb.util.ENDPOINT_IN
                    )
                    
                    if self.ep_out and self.ep_in:
                        self.device = device
                        self.connected = True
                        print("✓ Connected to device!")
                        return True
            except:
                continue
        
        print("✗ No USB device found")
        return False

    def send_command(self, cmd_type, *args):
        """Send command to device"""
        if not self.connected or not self.ep_out:
            return

        try:
            data = bytearray()
            data.append(cmd_type)
            
            if cmd_type == MOUSE_MOVE:
                x, y = args[0], args[1]
                data += struct.pack('>ff', x, y)
            elif cmd_type == MOUSE_CLICK:
                x, y, button = args[0], args[1], args[2]
                data.append(button)
                data += struct.pack('>ff', x, y)
            elif cmd_type == KEYBOARD:
                text = args[0]
                data += text.encode('utf-8')
            elif cmd_type == SCROLL:
                delta = args[0]
                data += struct.pack('>f', delta)
            
            self.ep_out.write(data)
        except Exception as e:
            print(f"Error sending command: {e}")
            self.connected = False

    def disconnect(self):
        """Disconnect from device"""
        if self.device:
            usb.util.release_interface(self.device, 0)
            usb.backend.libusb1.libusb_close(self.device.backend_handle)
            self.connected = False


def main():
    """Main function"""
    print("=" * 50)
    print("Extended Screen - PC Server")
    print("=" * 50)
    
    # Initialize USB controller
    usb_controller = UsbController()
    if not usb_controller.connect():
        print("Retrying in 3 seconds...")
        time.sleep(3)
        if not usb_controller.connect():
            print("Failed to connect. Make sure:")
            print("1. Android device is connected via USB")
            print("2. USB debugging is enabled")
            print("3. The app is running on the device")
            sys.exit(1)
    
    # Initialize input capture
    input_capture = InputCapture(lambda *args: usb_controller.send_command(*args))
    input_capture.start()
    
    print("✓ Input capture started")
    print("✓ Control your Android device from PC!")
    print("\nPress Ctrl+C to exit...")
    
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print("\nShutting down...")
        input_capture.stop()
        usb_controller.disconnect()
        print("Goodbye!")


if __name__ == '__main__':
    main()
