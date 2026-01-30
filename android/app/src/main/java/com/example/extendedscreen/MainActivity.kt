package com.example.extendedscreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var statusTextView: TextView
    private lateinit var enableAccessibilityBtn: Button
    private lateinit var startServiceBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusTextView = findViewById(R.id.status_text)
        enableAccessibilityBtn = findViewById(R.id.enable_accessibility_btn)
        startServiceBtn = findViewById(R.id.start_service_btn)

        updateStatus()

        enableAccessibilityBtn.setOnClickListener {
            openAccessibilitySettings()
        }

        startServiceBtn.setOnClickListener {
            startUsbService()
        }
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
    }

    private fun updateStatus() {
        val isAccessibilityEnabled = isAccessibilityServiceEnabled()
        statusTextView.text = if (isAccessibilityEnabled) {
            "✓ Accessibility Service: ENABLED"
        } else {
            "✗ Accessibility Service: DISABLED\n\nTap 'Enable Accessibility Service' to continue"
        }

        enableAccessibilityBtn.isEnabled = !isAccessibilityEnabled
        startServiceBtn.isEnabled = isAccessibilityEnabled
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val am = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabledServices.contains("com.example.extendedscreen/.AccessibilityControlService")
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }

    private fun startUsbService() {
        val serviceIntent = Intent(this, UsbControlService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        statusTextView.text = "✓ USB Service Started\n\nConnect your device via USB"
    }
}
