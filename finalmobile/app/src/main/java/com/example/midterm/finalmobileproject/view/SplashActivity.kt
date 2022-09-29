package com.example.midterm.finalmobileproject.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.midterm.finalmobileproject.MainActivity
import com.example.midterm.finalmobileproject.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var context: Context? = null
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(Manifest.permission.READ_PHONE_STATE)
                ActivityCompat.requestPermissions(this, permission, 0)
            } else {
                Access()
                return
            }
        } else {
            Access()
            return
        }
        context = this
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Access()
            return
        }
    }

    private fun Access() {
        val i = Intent(this, LoginActivity::class.java)
        // do something
        val handler = Handler()
        handler.postDelayed({
            startActivity(i)
            finish()
        }, 2000)
    }
}
