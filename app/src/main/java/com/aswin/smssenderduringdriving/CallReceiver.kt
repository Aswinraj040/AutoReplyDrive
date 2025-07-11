package com.aswin.smssenderduringdriving

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.media.AudioManager
import android.os.Handler
import android.os.Looper

class CallReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "CallReceiver"
        private const val PREFS_NAME = "driving_prefs"
        private const val KEY_LAST_NUMBER = "last_number"
        private const val KEY_LAST_TIME = "last_time"
        private const val SMS_COOLDOWN_MS = 10_000L // 10 seconds
    }

    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        val number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        Log.d(TAG, "Received call state: $state, number: $number")

        if (state == TelephonyManager.EXTRA_STATE_RINGING && !number.isNullOrEmpty()) {
            muteRingtone(context)
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val lastNumber = prefs.getString(KEY_LAST_NUMBER, "")
            val lastTime = prefs.getLong(KEY_LAST_TIME, 0L)
            val currentTime = System.currentTimeMillis()

            // Avoid duplicate SMS within cooldown window
            if (number == lastNumber && currentTime - lastTime < SMS_COOLDOWN_MS) {
                Log.d(TAG, "SMS already sent recently to $number, skipping...")
                return
            }

            val message = prefs.getString(
                "SMS_MESSAGE",
                "Sorry I am driving; will call you later — automated reply"
            )

            Log.d(TAG, "Sending SMS to $number with message: $message")

            try {
                SmsManager.getDefault().sendTextMessage(
                    number,
                    null,
                    message,
                    null,
                    null
                )
                Log.d(TAG, "SMS sent successfully")

                // Save state to avoid resending
                prefs.edit()
                    .putString(KEY_LAST_NUMBER, number)
                    .putLong(KEY_LAST_TIME, currentTime)
                    .apply()

            } catch (e: Exception) {
                Log.e(TAG, "SMS failed: ${e.message}", e)
            }
        }
    }
    private fun muteRingtone(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING)

        if (currentVolume > 0) {
            Log.d("CallReceiver", "Muting ringtone...")
            audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0)

            // Optionally restore volume after 15 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                Log.d("CallReceiver", "Restoring ringtone volume to $currentVolume")
                audioManager.setStreamVolume(AudioManager.STREAM_RING, currentVolume, 0)
            }, 35_000)
        }
    }
}
