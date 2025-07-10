package com.aswin.smssenderduringdriving

import android.app.*
import android.content.*
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class DrivingModeService : Service() {

    private lateinit var smsMessage: String
    private val callReceiver = CallReceiver()

    companion object {
        const val CHANNEL_ID = "driving_mode_channel"
        const val STOP_ACTION = "com.aswin.smssenderduringdriving.STOP_SERVICE"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == STOP_ACTION) {
            stopSelf()
            return START_NOT_STICKY
        }

        smsMessage = intent?.getStringExtra("SMS_MESSAGE")
            ?: "Sorry I am driving; will call you later — automated reply"

        // Register receiver
        val filter = IntentFilter("android.intent.action.PHONE_STATE")
        registerReceiver(callReceiver, filter)

        // Save message in shared prefs for use in receiver
        getSharedPreferences("driving_prefs", MODE_PRIVATE)
            .edit().putString("SMS_MESSAGE", smsMessage).apply()

        // Create notification with STOP button
        val stopIntent = Intent(this, DrivingModeService::class.java).apply {
            action = STOP_ACTION
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, createNotificationChannel())
            .setContentTitle("Driving mode on")
            .setContentText("Auto-reply enabled")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .addAction(0, "Stop", stopPendingIntent)
            .setOngoing(true)
            .build()

        startForeground(1, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        unregisterReceiver(callReceiver)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                CHANNEL_ID,
                "Driving Mode Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(chan)
        }
        return CHANNEL_ID
    }
}
