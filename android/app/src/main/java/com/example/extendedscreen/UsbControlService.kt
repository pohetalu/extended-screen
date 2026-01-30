package com.example.extendedscreen

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.io.InputStream
import java.io.OutputStream

class UsbControlService : Service() {
    private lateinit var usbManager: UsbManager
    private var usbDevice: UsbDevice? = null
    private var usbConnection: UsbDeviceConnection? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var isRunning = false

    companion object {
        private const val CHANNEL_ID = "UsbControlChannel"
        private const val NOTIFICATION_ID = 1

        // Command types
        private const val MOUSE_MOVE = 0x01
        private const val MOUSE_CLICK = 0x02
        private const val KEYBOARD = 0x03
        private const val SCROLL = 0x04
    }

    override fun onCreate() {
        super.onCreate()
        usbManager = getSystemService(USB_SERVICE) as UsbManager
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        isRunning = true
        Thread { initializeUsb() }.start()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "USB Control Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Extended Screen")
            .setContentText("USB Service Running")
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun initializeUsb() {
        try {
            val devices = usbManager.deviceList.values
            usbDevice = devices.find { it.vendorId == 0x0604 || it.deviceId > 0 }

            if (usbDevice != null) {
                usbConnection = usbManager.openDevice(usbDevice)
                if (usbConnection != null) {
                    inputStream = usbConnection?.inputStream
                    outputStream = usbConnection?.outputStream
                    readCommands()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun readCommands() {
        try {
            while (isRunning && inputStream != null) {
                val buffer = ByteArray(1024)
                val bytesRead = inputStream?.read(buffer) ?: -1

                if (bytesRead > 0) {
                    parseCommand(buffer, bytesRead)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun parseCommand(buffer: ByteArray, length: Int) {
        if (length < 1) return

        when (buffer[0].toInt()) {
            MOUSE_MOVE -> {
                if (length >= 9) {
                    val x = java.nio.ByteBuffer.wrap(buffer, 1, 4).float
                    val y = java.nio.ByteBuffer.wrap(buffer, 5, 4).float
                    sendBroadcast("MOUSE_MOVE", x, y)
                }
            }
            MOUSE_CLICK -> {
                if (length >= 2) {
                    val button = buffer[1].toInt()
                    sendBroadcast("MOUSE_CLICK", button.toFloat(), 0f)
                }
            }
            KEYBOARD -> {
                if (length > 1) {
                    val text = String(buffer, 1, length - 1)
                    sendBroadcast("KEYBOARD", text)
                }
            }
            SCROLL -> {
                if (length >= 5) {
                    val delta = java.nio.ByteBuffer.wrap(buffer, 1, 4).float
                    sendBroadcast("SCROLL", delta, 0f)
                }
            }
        }
    }

    private fun sendBroadcast(command: String, x: Float, y: Float) {
        val intent = Intent("com.example.extendedscreen.CONTROL_COMMAND")
        intent.putExtra("command", command)
        intent.putExtra("x", x)
        intent.putExtra("y", y)
        sendBroadcast(intent)
    }

    private fun sendBroadcast(command: String, text: String) {
        val intent = Intent("com.example.extendedscreen.CONTROL_COMMAND")
        intent.putExtra("command", command)
        intent.putExtra("text", text)
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        inputStream?.close()
        outputStream?.close()
        usbConnection?.close()
    }
}
