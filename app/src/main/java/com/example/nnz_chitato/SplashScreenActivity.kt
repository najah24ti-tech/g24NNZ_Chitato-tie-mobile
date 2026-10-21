package com.example.nnz_chitato

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        lifecycleScope.launch {
            delay(2000)

            val sharedPref =
                getSharedPreferences("user_pref", MODE_PRIVATE)

            val isLogin = sharedPref.getBoolean("isLogin", false)

            val tujuan = if (isLogin) {
                BaseActivity::class.java
            } else {
                AuthActivity::class.java
            }

            val intent = Intent(this@SplashScreenActivity, tujuan)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }
}
