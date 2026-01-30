package com.example.extendedscreen

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Path
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.ContextCompat

class AccessibilityControlService : AccessibilityService() {
    private lateinit var broadcastReceiver: BroadcastReceiver
    private var screenWidth = 0
    private var screenHeight = 0

    override fun onCreate() {
        super.onCreate()
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let { handleCommand(it) }
            }
        }

        val filter = IntentFilter("com.example.extendedscreen.CONTROL_COMMAND")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.registerReceiver(
                this,
                broadcastReceiver,
                filter,
                ContextCompat.RECEIVER_EXPORTED
            )
        } else {
            registerReceiver(broadcastReceiver, filter)
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    override fun onServiceConnected() {
        super.onServiceConnected()
        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels
    }

    private fun handleCommand(intent: Intent) {
        val command = intent.getStringExtra("command") ?: return

        when (command) {
            "MOUSE_MOVE" -> {
                val x = intent.getFloatExtra("x", 0f)
                val y = intent.getFloatExtra("y", 0f)
                performMouseMove(x, y)
            }
            "MOUSE_CLICK" -> {
                val button = intent.getIntExtra("x", 1).toInt()
                val x = intent.getFloatExtra("x", 0.5f)
                val y = intent.getFloatExtra("y", 0.5f)
                performMouseClick(x, y, button)
            }
            "KEYBOARD" -> {
                val text = intent.getStringExtra("text") ?: return
                performKeyboardInput(text)
            }
            "SCROLL" -> {
                val delta = intent.getFloatExtra("x", 0f)
                performScroll(delta)
            }
        }
    }

    private fun performMouseMove(normalizedX: Float, normalizedY: Float) {
        val x = (normalizedX * screenWidth).toInt()
        val y = (normalizedY * screenHeight).toInt()
        // Store for next click
    }

    private fun performMouseClick(normalizedX: Float, normalizedY: Float, button: Int) {
        val x = (normalizedX * screenWidth).toFloat()
        val y = (normalizedY * screenHeight).toFloat()

        val path = Path().apply {
            moveTo(x, y)
            lineTo(x, y)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 100))
            .build()

        dispatchGesture(gesture, null, null)
    }

    private fun performKeyboardInput(text: String) {
        // Use clipboard paste method
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("text", text)
        clipboard.setPrimaryClip(clip)

        // Trigger paste via keyboard - this requires accessibility permission
        // For now, just copy to clipboard
    }

    private fun performScroll(delta: Float) {
        val path = Path().apply {
            moveTo(resources.displayMetrics.widthPixels / 2f, resources.displayMetrics.heightPixels / 2f)
            lineTo(resources.displayMetrics.widthPixels / 2f, resources.displayMetrics.heightPixels / 2f + delta * 100)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 200))
            .build()

        dispatchGesture(gesture, null, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}
