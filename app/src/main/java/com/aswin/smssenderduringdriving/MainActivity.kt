package com.aswin.smssenderduringdriving

import android.Manifest
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val SMS_MESSAGE =
        "Sorry I am driving; will call you later — automated reply"

    private val requiredPermissions = mutableListOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.SEND_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_CALL_LOG
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }.toTypedArray()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }

        if (allGranted) {
            startDrivingModeService()
        } else {
            // You can notify the user that all permissions are required
        }
    }

    private val roleRequestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // No action needed here unless you want to check if role was granted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startBtn = findViewById<Button>(R.id.button_start)
        val stopBtn = findViewById<Button>(R.id.button_stop)

        startBtn.setOnClickListener {
            requestDefaultDialer() // Request role first
            requestAllPermissions() // Then request permissions
        }

        stopBtn.setOnClickListener {
            val intent = Intent(this, DrivingModeService::class.java)
            intent.action = DrivingModeService.STOP_ACTION
            ContextCompat.startForegroundService(this, intent)
        }
    }

    private fun requestAllPermissions() {
        val notGranted = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isNotEmpty()) {
            permissionLauncher.launch(notGranted.toTypedArray())
        } else {
            startDrivingModeService()
        }
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

    private fun startDrivingModeService() {
        val intent = Intent(this, DrivingModeService::class.java).apply {
            putExtra("SMS_MESSAGE", SMS_MESSAGE)
        }
        ContextCompat.startForegroundService(this, intent)
    }
}
