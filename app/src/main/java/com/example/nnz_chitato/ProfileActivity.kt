
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
        val sharedPref = getSharedPreferences(
            "user_pref",
            MODE_PRIVATE
        )

        val username = sharedPref.getString(
            "username",
            "Pengguna"
        ) ?: "Pengguna"

        binding.txtProfileUsername.text = username
        binding.txtProfileName.text = username

        // Memperbarui teks tombol sesuai tema aktif
        updateThemeButton()

        // Tombol Ganti Mode Gelap
        binding.btnDarkMode.setOnClickListener {
            ThemeManager.toggleTheme(this)

            // Tema akan diterapkan dan Activity biasanya dibuat ulang
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
                Intent(
                    this,
                    DeveloperProfileActivity::class.java
                )
            )
        }

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah kamu yakin ingin logout?")
                .setPositiveButton("Ya") { _, _ ->

                    // Hapus data login saja
                    sharedPref.edit {
                        clear()
                    }

                    // Kembali ke halaman Login
                    val intent = Intent(
                        this,
                        AuthActivity::class.java
                    )

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

    private fun updateThemeButton() {
        binding.btnDarkMode.text =
            if (ThemeManager.isDarkMode(this)) {
                "Aktifkan Mode Terang"
            } else {
                "Aktifkan Mode Gelap"
            }
    }
}
