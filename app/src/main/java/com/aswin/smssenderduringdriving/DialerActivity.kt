package com.aswin.smssenderduringdriving

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DialerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Forward the call intent to the system dialer
        val intent = Intent(Intent.ACTION_CALL, intent.data)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        startActivity(intent)

        // Close this fallback dialer
        finish()
    }
}
