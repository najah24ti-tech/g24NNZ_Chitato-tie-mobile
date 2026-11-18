
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

        // Membuka halaman Register
        binding.btnKeRegistrasi.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }

        // Proses Login
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

            val sharedPref = getSharedPreferences(
                "user_pref",
                MODE_PRIVATE
            )

            val savedUsername =
                sharedPref.getString("username", "") ?: ""

            val savedEmail =
                sharedPref.getString("email", "") ?: ""

            val savedPassword =
                sharedPref.getString("password", "") ?: ""

            // Akun lama
            val akunLama =
                user == "najah" && pass == "12345"

            // Akun hasil registrasi
            val akunTerdaftar =
                (user == savedUsername || user == savedEmail) &&
                        pass == savedPassword &&
                        savedUsername.isNotEmpty() &&
                        savedPassword.isNotEmpty()

            if (akunLama || akunTerdaftar) {

                val usernameLogin = if (akunLama) {
                    "najah"
                } else {
                    savedUsername
                }

                // Simpan status login tanpa menghapus data registrasi
                val berhasil = sharedPref.edit()
                    .putBoolean("isLogin", true)
                    .putString("username", usernameLogin)
                    .commit()

                if (!berhasil) {
                    Toast.makeText(
                        this,
                        "Gagal menyimpan status login",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                Toast.makeText(
                    this,
                    "Login berhasil",
                    Toast.LENGTH_SHORT
                ).show()

                // Masuk ke Dashboard
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
                Toast.makeText(
                    this,
                    "Username/email atau password salah",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
