
package com.example.nnz_chitato

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.nnz_chitato.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol menuju halaman registrasi
        binding.btnKeRegistrasi.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }

        // Proses login
        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text
                .toString()
                .trim()

            val password = binding.edtPassword.text
                .toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Username dan password harus diisi",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Ambil data akun yang tersimpan saat registrasi
            val sharedPref = getSharedPreferences(
                "user_pref",
                MODE_PRIVATE
            )

            val savedUsername =
                sharedPref.getString("username", "")

            val savedPassword =
                sharedPref.getString("password", "")

            // Validasi login menggunakan data registrasi
            if (
                username == savedUsername &&
                password == savedPassword &&
                !savedUsername.isNullOrEmpty() &&
                !savedPassword.isNullOrEmpty()
            ) {
                sharedPref.edit()
                    .putBoolean("isLogin", true)
                    .apply()

                Toast.makeText(
                    this,
                    "Login berhasil",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(
                    this,
                    BaseActivity::class.java
                )

                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish()

            } else {
                AlertDialog.Builder(this)
                    .setTitle("Login Gagal")
                    .setMessage("Username atau password salah")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }
}
