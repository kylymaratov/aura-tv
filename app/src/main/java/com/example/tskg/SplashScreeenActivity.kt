package com.example.tskg

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.app.Activity
import android.content.pm.PackageManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashTheme)
        setContentView(R.layout.activity_splash)

        val isTv = packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if (isTv) {
                Intent(this, com.example.tskg.tv.MainActivity::class.java)
            } else {
                Intent(this, com.example.tskg.mobile.MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 1500)
    }
}