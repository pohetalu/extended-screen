package com.extendedscreen.android.service

import android.app.*
import android.content.Intent
import android.hardware.usb.UsbAccessory
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.IBinder
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.extendedscreen.android.MainActivity
import com.extendedscreen.android.R
import kotlinx.coroutines.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer

class UsbControlService : Service() {
    
    private var serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var usbAccessory: UsbAccessory? = null
    private var fileDescriptor: ParcelFileDescriptor? = null
    private var inputStream: FileInputStream? = null
    private var outputStream: FileOutputStream? = null
    
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "UsbControlChannel"
        private const val BUFFER_SIZE = 16384
        
        // Command types
        private const val CMD_MOUSE_MOVE = 0x01.toByte()
        private const val CMD_MOUSE_CLICK = 0x02.toByte()
        private const val CMD_KEYBOARD = 0x03.toByte()
        private const val CMD_SCROLL = 0x04.toByte()
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification("Waiting for USB connection...")
        startForeground(NOTIFICATION_ID, notification)
        
        startUsbCommunication()
        
        return START_STICKY
    }
    
    private fun startUsbCommunication() {
        serviceScope.launch {
            try {
                val usbManager = getSystemService(USB_SERVICE) as UsbManager
                val accessoryList = usbManager.accessoryList
                
                if (accessoryList != null && accessoryList.isNotEmpty()) {
                    usbAccessory = accessoryList[0]
                    fileDescriptor = usbManager.openAccessory(usbAccessory)
                    
                    fileDescriptor?.let { fd ->
                        inputStream = FileInputStream(fd.fileDescriptor)
                        outputStream = FileOutputStream(fd.fileDescriptor)
                        
                        updateNotification("Connected via USB")
                        readCommands()
                    }
                } else {
                    updateNotification("No USB accessory detected")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                updateNotification("Error: ${e.message}")
            }
        }
    }
    
    private suspend fun readCommands() = withContext(Dispatchers.IO) {
        val buffer = ByteArray(BUFFER_SIZE)
        
        try {
            while (isActive) {
                val bytesRead = inputStream?.read(buffer) ?: -1
                if (bytesRead > 0) {
                    processCommand(buffer, bytesRead)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            updateNotification("Connection lost")
        }
    }
    
    private fun processCommand(data: ByteArray, length: Int) {
        if (length < 1) return
        
        val commandType = data[0]
        val payload = data.copyOfRange(1, length)
        
        when (commandType) {
            CMD_MOUSE_MOVE -> handleMouseMove(payload)
            CMD_MOUSE_CLICK -> handleMouseClick(payload)
            CMD_KEYBOARD -> handleKeyboard(payload)
            CMD_SCROLL -> handleScroll(payload)
        }
    }
    
    private fun handleMouseMove(payload: ByteArray) {
        if (payload.size < 8) return
        
        val buffer = ByteBuffer.wrap(payload)
        val x = buffer.float
        val y = buffer.float
        
        // Send coordinates to accessibility service
        val intent = Intent("com.extendedscreen.MOUSE_MOVE")
        intent.putExtra("x", x)
        intent.putExtra("y", y)
        sendBroadcast(intent)
    }
    
    private fun handleMouseClick(payload: ByteArray) {
        if (payload.size < 1) return
        
        val button = payload[0].toInt()
        
        val intent = Intent("com.extendedscreen.MOUSE_CLICK")
        intent.putExtra("button", button)
        sendBroadcast(intent)
    }
    
    private fun handleKeyboard(payload: ByteArray) {
        if (payload.isEmpty()) return
        
        val text = String(payload, Charsets.UTF_8)
        
        val intent = Intent("com.extendedscreen.KEYBOARD")
        intent.putExtra("text", text)
        sendBroadcast(intent)
    }
    
    private fun handleScroll(payload: ByteArray) {
        if (payload.size < 4) return
        
        val buffer = ByteBuffer.wrap(payload)
        val delta = buffer.float
        
        val intent = Intent("com.extendedscreen.SCROLL")
        intent.putExtra("delta", delta)
        sendBroadcast(intent)
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "USB Control Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(text: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Extended Screen")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .build()
    }
    
    private fun updateNotification(text: String) {
        val notification = createNotification(text)
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        
        try {
            inputStream?.close()
            outputStream?.close()
            fileDescriptor?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}
