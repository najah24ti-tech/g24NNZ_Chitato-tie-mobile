
package com.example.nnz_chitato

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nnz_chitato.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Membuka halaman registrasi
        binding.btnKeRegistrasi.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }

        // Proses login
        binding.btnLogin.setOnClickListener {
            val user = binding.edtUsername.text.toString().trim()
            val pass = binding.edtPassword.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(
                    this,
                    "Username dan password harus diisi",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val sharedPref =
                getSharedPreferences("user_pref", MODE_PRIVATE)

            val savedUsername = sharedPref.getString("username", "")
            val savedEmail = sharedPref.getString("email", "")
            val savedPassword = sharedPref.getString("password", "")

            // Pertahankan akun lama dan dukung akun hasil registrasi
            val loginBerhasil =
                (user == "najah" && pass == "12345") ||
                        (
                                (user == savedUsername || user == savedEmail) &&
                                        pass == savedPassword &&
                                        !savedUsername.isNullOrEmpty() &&
                                        !savedPassword.isNullOrEmpty()
                                )

            if (loginBerhasil) {
                val usernameLogin = if (user == "najah") {
                    "najah"
                } else {
                    savedUsername ?: user
                }

                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("username", usernameLogin)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Username/email atau password salah",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
