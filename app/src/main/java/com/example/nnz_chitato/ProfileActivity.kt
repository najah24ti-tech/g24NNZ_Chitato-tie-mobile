package com.example.nnz_chitato

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.nnz_chitato.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengambil username dari SharedPreferences
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val username = sharedPref.getString("username", "Pengguna") ?: "Pengguna"

        binding.txtProfileUsername.text = username
        binding.txtProfileName.text = username

        // Tombol Home
        binding.navHome.setOnClickListener {
            finish()
        }

        // Tombol Dashboard
        binding.navHome.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        // Tombol Sales
        binding.navSales.setOnClickListener {
            startActivity(Intent(this, SalesActivity::class.java))
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

        // Tombol About Developer
        binding.btnDeveloperProfile.setOnClickListener {
            startActivity(
                Intent(this, DeveloperProfileActivity::class.java)
            )
        }

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah kamu yakin ingin logout?")
                .setPositiveButton("Ya") { _, _ ->
                    // Menghapus data login
                    sharedPref.edit {
                        clear()
                    }

                    // Kembali ke halaman Login
                    val intent = Intent(this, AuthActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Tidak", null)
                .show()
        }
    }
}
