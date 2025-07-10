package com.aswin.smssenderduringdriving

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        val number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        if (state == TelephonyManager.EXTRA_STATE_RINGING && !number.isNullOrEmpty()) {
            val prefs = context.getSharedPreferences("driving_prefs", Context.MODE_PRIVATE)
            val message = prefs.getString(
                "SMS_MESSAGE",
                "Sorry I am driving; will call you later — automated reply"
            )

            SmsManager.getDefault().sendTextMessage(
                number,
                null,
                message,
                null,
                null
            )
        }
    }
}
