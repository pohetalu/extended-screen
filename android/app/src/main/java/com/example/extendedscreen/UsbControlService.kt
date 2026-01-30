package com.example.extendedscreen

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint
import android.hardware.usb.UsbInterface
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlin.concurrent.thread

class UsbControlService : Service() {
    private lateinit var usbManager: UsbManager
    private var usbDevice: UsbDevice? = null
    private var usbConnection: UsbDeviceConnection? = null
    private var inputEndpoint: UsbEndpoint? = null
    private var isRunning = false
    private var readThread: Thread? = null

    companion object {
        private const val CHANNEL_ID = "UsbControlChannel"
        private const val NOTIFICATION_ID = 1
        private const val TAG = "UsbControlService"
    }

    override fun onCreate() {
        super.onCreate()
        usbManager = getSystemService(USB_SERVICE) as UsbManager
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        isRunning = true
        thread { initializeUsb() }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "USB Control", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
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
            usbDevice = usbManager.deviceList.values.firstOrNull()
            if (usbDevice != null) {
                usbConnection = usbManager.openDevice(usbDevice)
                if (usbConnection != null) {
                    findEndpoints()
                    startReadThread()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "USB init error", e)
        }
    }

    private fun findEndpoints() {
        val device = usbDevice ?: return
        try {
            if (device.interfaceCount > 0) {
                val intf: UsbInterface = device.getInterface(0)
                usbConnection?.claimInterface(intf, true)
                for (i in 0 until intf.endpointCount) {
                    val endpoint: UsbEndpoint = intf.getEndpoint(i)
                    if (endpoint.direction == UsbEndpoint.USB_DIR_IN) {
                        inputEndpoint = endpoint
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Endpoint error", e)
        }
    }

    private fun startReadThread() {
        readThread = thread {
            try {
                while (isRunning && inputEndpoint != null && usbConnection != null) {
                    val buffer = ByteArray(64)
                    val bytesRead = usbConnection?.bulkTransfer(inputEndpoint, buffer, buffer.size, 1000) ?: -1
                    if (bytesRead > 0) {
                        parseCommand(buffer, bytesRead)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Read error", e)
            }
        }
    }

    private fun parseCommand(buffer: ByteArray, length: Int) {
        if (length < 1) return
        val cmd = Intent("com.example.extendedscreen.CONTROL_COMMAND")
        
        when (buffer[0].toInt()) {
            0x01 -> if (length >= 9) {
                cmd.putExtra("command", "MOUSE_MOVE")
                cmd.putExtra("x", java.nio.ByteBuffer.wrap(buffer, 1, 4).float)
                cmd.putExtra("y", java.nio.ByteBuffer.wrap(buffer, 5, 4).float)
                sendBroadcast(cmd)
            }
            0x02 -> if (length >= 2) {
                cmd.putExtra("command", "MOUSE_CLICK")
                cmd.putExtra("button", buffer[1].toInt())
                sendBroadcast(cmd)
            }
            0x03 -> if (length > 1) {
                cmd.putExtra("command", "KEYBOARD")
                cmd.putExtra("text", String(buffer, 1, length - 1, Charsets.UTF_8))
                sendBroadcast(cmd)
            }
            0x04 -> if (length >= 5) {
                cmd.putExtra("command", "SCROLL")
                cmd.putExtra("delta", java.nio.ByteBuffer.wrap(buffer, 1, 4).float)
                sendBroadcast(cmd)
            }
        }
    }

    override fun onDestroy() {
        isRunning = false
        readThread?.join(500)
        usbConnection?.close()
        super.onDestroy()
    }
}
