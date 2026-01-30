package com.extendedscreen.android

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.extendedscreen.android.service.UsbControlService

class MainActivity : AppCompatActivity() {
    
    private lateinit var statusText: TextView
    private lateinit var startButton: Button
    private lateinit var accessibilityButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        statusText = findViewById(R.id.statusText)
        startButton = findViewById(R.id.startButton)
        accessibilityButton = findViewById(R.id.accessibilityButton)
        
        startButton.setOnClickListener {
            startService()
        }
        
        accessibilityButton.setOnClickListener {
            openAccessibilitySettings()
        }
        
        updateStatus()
    }
    
    override fun onResume() {
        super.onResume()
        updateStatus()
    }
    
    private fun startService() {
        val intent = Intent(this, UsbControlService::class.java)
        startForegroundService(intent)
        updateStatus()
    }
    
    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }
    
    private fun updateStatus() {
        val isAccessibilityEnabled = isAccessibilityServiceEnabled()
        
        if (isAccessibilityEnabled) {
            statusText.text = "Status: Ready to connect via USB"
            startButton.isEnabled = true
            accessibilityButton.text = "Accessibility Service: Enabled"
        } else {
            statusText.text = "Status: Please enable Accessibility Service"
            startButton.isEnabled = false
            accessibilityButton.text = "Enable Accessibility Service"
        }
    }
    
    private fun isAccessibilityServiceEnabled(): Boolean {
        val serviceName = "${packageName}/.service.AccessibilityControlService"
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        return enabledServices?.contains(serviceName) == true
    }
}
