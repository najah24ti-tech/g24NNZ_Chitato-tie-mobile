package com.example.nnz_chitato

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nnz_chitato.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengatur warna status bar dan ikon sistem
        window.statusBarColor = Color.rgb(231, 237, 255)
        window.navigationBarColor = Color.rgb(231, 237, 255)

        WindowCompat.getInsetsController(
            window,
            window.decorView
        ).isAppearanceLightStatusBars = true

        WindowCompat.getInsetsController(
            window,
            window.decorView
        ).isAppearanceLightNavigationBars = true

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengambil username dari SharedPreferences
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val username = sharedPref.getString("username", "Pengguna") ?: "Pengguna"

        binding.txtWelcome.text = "Hi, $username!"

        // Memberi jarak konten dari status bar dan navigasi sistem
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Tombol Home
        binding.navHome.setOnClickListener {
            // Tetap di halaman Dashboard
        }

        // Tombol Sales
        binding.navSales.setOnClickListener {
            startActivity(Intent(this, SalesActivity::class.java))
        }

        // Tombol Profile
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
