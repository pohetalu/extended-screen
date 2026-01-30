package com.extendedscreen.android.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Path
import android.util.DisplayMetrics
import android.view.accessibility.AccessibilityEvent
import android.view.inputmethod.InputConnection
import kotlinx.coroutines.*

class AccessibilityControlService : AccessibilityService() {
    
    private var currentX = 0f
    private var currentY = 0f
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    private val commandReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                "com.extendedscreen.MOUSE_MOVE" -> {
                    val x = intent.getFloatExtra("x", 0f)
                    val y = intent.getFloatExtra("y", 0f)
                    handleMouseMove(x, y)
                }
                "com.extendedscreen.MOUSE_CLICK" -> {
                    val button = intent.getIntExtra("button", 0)
                    handleMouseClick(button)
                }
                "com.extendedscreen.KEYBOARD" -> {
                    val text = intent.getStringExtra("text") ?: ""
                    handleKeyboard(text)
                }
                "com.extendedscreen.SCROLL" -> {
                    val delta = intent.getFloatExtra("delta", 0f)
                    handleScroll(delta)
                }
            }
        }
    }
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        
        val filter = IntentFilter().apply {
            addAction("com.extendedscreen.MOUSE_MOVE")
            addAction("com.extendedscreen.MOUSE_CLICK")
            addAction("com.extendedscreen.KEYBOARD")
            addAction("com.extendedscreen.SCROLL")
        }
        registerReceiver(commandReceiver, filter, RECEIVER_EXPORTED)
        
        // Get screen dimensions
        val metrics = resources.displayMetrics
        currentX = metrics.widthPixels / 2f
        currentY = metrics.heightPixels / 2f
    }
    
    private fun handleMouseMove(x: Float, y: Float) {
        // Convert normalized coordinates (0-1) to screen coordinates
        val metrics = resources.displayMetrics
        currentX = x * metrics.widthPixels
        currentY = y * metrics.heightPixels
        
        // Update cursor position (if using custom cursor overlay)
        // For now, just store the position for click events
    }
    
    private fun handleMouseClick(button: Int) {
        serviceScope.launch {
            performClick(currentX, currentY)
        }
    }
    
    private fun handleKeyboard(text: String) {
        // Try to input text using current input connection
        val rootNode = rootInActiveWindow ?: return
        
        try {
            // Find focused text field
            val focusedNode = rootNode.findFocus(AccessibilityEvent.FOCUS_INPUT)
            
            if (focusedNode != null) {
                // Use clipboard to paste text
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = android.content.ClipData.newPlainText("text", text)
                clipboard.setPrimaryClip(clip)
                
                focusedNode.performAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_PASTE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun handleScroll(delta: Float) {
        serviceScope.launch {
            val startY = currentY
            val endY = currentY + (delta * 100)
            performSwipe(currentX, startY, currentX, endY)
        }
    }
    
    private suspend fun performClick(x: Float, y: Float) = withContext(Dispatchers.Main) {
        val path = Path().apply {
            moveTo(x, y)
        }
        
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 100))
            .build()
        
        dispatchGesture(gesture, null, null)
    }
    
    private suspend fun performSwipe(x1: Float, y1: Float, x2: Float, y2: Float) = withContext(Dispatchers.Main) {
        val path = Path().apply {
            moveTo(x1, y1)
            lineTo(x2, y2)
        }
        
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 300))
            .build()
        
        dispatchGesture(gesture, null, null)
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Handle accessibility events if needed
    }
    
    override fun onInterrupt() {
        // Handle service interruption
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        try {
            unregisterReceiver(commandReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
