package com.example.nnz_chitato

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nnz_chitato.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol Home
        binding.navHome.setOnClickListener {
            finish()
        }

        // Tombol Dashboard
        binding.navDashboard.setOnClickListener {
            finish()
        }

        // Tombol Profile
        binding.navProfile.setOnClickListener {
            // Tetap di halaman Profile
        }

        // Tombol Edit Profile
        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(
                this,
                "Fitur Edit Profile belum tersedia",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            finishAffinity()
        }
    }
}
