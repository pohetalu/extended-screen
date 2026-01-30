#!/usr/bin/env python3
"""
PC Server for Android Extended Screen
Captures mouse and keyboard events and sends to Android device via USB
"""

import sys
import struct
import time
from pynput import mouse, keyboard
import usb.core
import usb.util
from threading import Thread, Lock

# Command types (must match Android app)
CMD_MOUSE_MOVE = 0x01
CMD_MOUSE_CLICK = 0x02
CMD_KEYBOARD = 0x03
CMD_SCROLL = 0x04

class USBController:
    def __init__(self):
        self.device = None
        self.endpoint_out = None
        self.endpoint_in = None
        self.lock = Lock()
        self.running = False
        
    def connect(self):
        """Connect to Android device via USB"""
        print("Looking for Android device...")
        
        # Find Android device (you may need to adjust vendor_id and product_id)
        # Common Android vendor IDs: Google: 0x18d1, Samsung: 0x04e8
        self.device = usb.core.find(idVendor=0x18d1)
        
        if self.device is None:
            print("No Android device found!")
            return False
            
        try:
            # Set configuration
            self.device.set_configuration()
            
            # Get active configuration
            cfg = self.device.get_active_configuration()
            intf = cfg[(0, 0)]
            
            # Find endpoints
            self.endpoint_out = usb.util.find_descriptor(
                intf,
                custom_match=lambda e: usb.util.endpoint_direction(e.bEndpointAddress) == usb.util.ENDPOINT_OUT
            )
            
            self.endpoint_in = usb.util.find_descriptor(
                intf,
                custom_match=lambda e: usb.util.endpoint_direction(e.bEndpointAddress) == usb.util.ENDPOINT_IN
            )
            
            if self.endpoint_out is None or self.endpoint_in is None:
                print("Could not find USB endpoints")
                return False
                
            print(f"Connected to device: {self.device.manufacturer} {self.device.product}")
            self.running = True
            return True
            
        except Exception as e:
            print(f"Error connecting to device: {e}")
            return False
    
    def send_command(self, cmd_type, payload):
        """Send command to Android device"""
        if not self.running or self.endpoint_out is None:
            return
            
        with self.lock:
            try:
                # Combine command type and payload
                data = bytes([cmd_type]) + payload
                self.endpoint_out.write(data)
            except Exception as e:
                print(f"Error sending command: {e}")
    
    def disconnect(self):
        """Disconnect from device"""
        self.running = False
        if self.device:
            usb.util.dispose_resources(self.device)
            self.device = None
        print("Disconnected from device")

class InputCapture:
    def __init__(self, usb_controller):
        self.usb = usb_controller
        self.screen_width = 1920  # Adjust to your screen resolution
        self.screen_height = 1080
        self.mouse_listener = None
        self.keyboard_listener = None
        
    def on_mouse_move(self, x, y):
        """Handle mouse movement"""
        # Normalize coordinates to 0-1 range
        norm_x = x / self.screen_width
        norm_y = y / self.screen_height
        
        # Pack as floats
        payload = struct.pack('ff', norm_x, norm_y)
        self.usb.send_command(CMD_MOUSE_MOVE, payload)
    
    def on_mouse_click(self, x, y, button, pressed):
        """Handle mouse clicks"""
        if pressed:
            # Map pynput button to our protocol
            button_id = 0
            if button == mouse.Button.left:
                button_id = 1
            elif button == mouse.Button.right:
                button_id = 2
            elif button == mouse.Button.middle:
                button_id = 3
                
            payload = bytes([button_id])
            self.usb.send_command(CMD_MOUSE_CLICK, payload)
    
    def on_mouse_scroll(self, x, y, dx, dy):
        """Handle mouse scroll"""
        # Send vertical scroll delta
        payload = struct.pack('f', float(dy))
        self.usb.send_command(CMD_SCROLL, payload)
    
    def on_key_press(self, key):
        """Handle keyboard press"""
        try:
            # Get character if alphanumeric
            if hasattr(key, 'char') and key.char:
                text = key.char
            else:
                # Handle special keys
                text = str(key).replace('Key.', '')
                
            payload = text.encode('utf-8')
            self.usb.send_command(CMD_KEYBOARD, payload)
            
        except Exception as e:
            print(f"Error handling key press: {e}")
    
    def start(self):
        """Start capturing input events"""
        print("Starting input capture...")
        print("Press Ctrl+C to stop")
        
        # Start mouse listener
        self.mouse_listener = mouse.Listener(
            on_move=self.on_mouse_move,
            on_click=self.on_mouse_click,
            on_scroll=self.on_mouse_scroll
        )
        self.mouse_listener.start()
        
        # Start keyboard listener
        self.keyboard_listener = keyboard.Listener(
            on_press=self.on_key_press
        )
        self.keyboard_listener.start()
        
        print("Input capture started!")
    
    def stop(self):
        """Stop capturing input events"""
        if self.mouse_listener:
            self.mouse_listener.stop()
        if self.keyboard_listener:
            self.keyboard_listener.stop()
        print("Input capture stopped")

def main():
    print("=== Android Extended Screen - PC Server ===")
    print()
    
    # Create USB controller
    usb_controller = USBController()
    
    # Connect to device
    if not usb_controller.connect():
        print("Failed to connect to Android device")
        print("\nMake sure:")
        print("1. Phone is connected via USB")
        print("2. USB debugging is enabled")
        print("3. Android app is running")
        print("4. You have proper USB permissions")
        return 1
    
    # Create input capture
    input_capture = InputCapture(usb_controller)
    
    try:
        # Start capturing input
        input_capture.start()
        
        # Keep running
        while True:
            time.sleep(1)
            
    except KeyboardInterrupt:
        print("\nShutting down...")
    finally:
        input_capture.stop()
        usb_controller.disconnect()
    
    return 0

if __name__ == "__main__":
    sys.exit(main())
