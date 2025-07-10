package com.aswin.smssenderduringdriving

import android.Manifest
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val SMS_MESSAGE =
        "Sorry I am driving; will call you later — automated reply"

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startDrivingModeService()
        } else {
            // Optional: Show a message or disable feature
        }
    }

    private val roleRequestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // You can optionally check if the role was granted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startBtn = findViewById<Button>(R.id.button_start)
        val stopBtn = findViewById<Button>(R.id.button_stop)

        startBtn.setOnClickListener {
            requestDefaultDialer()
            requestNotificationPermission()
        }

        stopBtn.setOnClickListener {
            val intent = Intent(this, DrivingModeService::class.java)
            intent.action = DrivingModeService.STOP_ACTION
            ContextCompat.startForegroundService(this, intent)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }
        startDrivingModeService()
    }

    private fun startDrivingModeService() {
        val intent = Intent(this, DrivingModeService::class.java).apply {
            putExtra("SMS_MESSAGE", SMS_MESSAGE)
        }
        ContextCompat.startForegroundService(this, intent)
    }

    private fun requestDefaultDialer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
            if (!roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                roleRequestLauncher.launch(intent)
            }
        }
    }
}
