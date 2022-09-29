package com.midterm.qr_app.callBacks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val i = Intent(this, MainActivity::class.java)
        val handler = Handler()
        handler.postDelayed({
            startActivity(i)
            finish()
        }, 2000)
    }
}