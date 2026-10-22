package com.example.nnz_chitato

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nnz_chitato.databinding.ActivityVerifikasiBinding

class VerifikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifikasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tampilkanData()

        // Tombol Kembali ke halamabinding.btnSubmit.setOnClickListener {n registrasi
        binding.btnKembali.setOnClickListener {
            finish()
        }

        // Tombol Submit menuju Dashboard
        binding.btnSubmit.setOnClickListener {
            validasiData()
        }
    }

    private fun tampilkanData() {
        val pref = getSharedPreferences("user_pref", MODE_PRIVATE)

        binding.txtNama.text =
            pref.getString("nama", "").orEmpty()

        binding.txtEmail.text =
            pref.getString("email", "").orEmpty()

        binding.txtTanggalLahir.text =
            pref.getString("tanggalLahir", "").orEmpty()

        binding.txtJenisKelamin.text =
            pref.getString("jenisKelamin", "").orEmpty()

        binding.txtUsername.text =
            pref.getString("username", "").orEmpty()

        binding.txtPassword.text =
            pref.getString("password", "").orEmpty()

        binding.txtConfirmPassword.text =
            pref.getString("confirmPassword", "").orEmpty()
    }

    private fun validasiData() {
        val pref = getSharedPreferences("user_pref", MODE_PRIVATE)

        val nama = pref.getString("nama", "").orEmpty()
        val email = pref.getString("email", "").orEmpty()
        val tanggalLahir =
            pref.getString("tanggalLahir", "").orEmpty()
        val jenisKelamin =
            pref.getString("jenisKelamin", "").orEmpty()
        val username = pref.getString("username", "").orEmpty()
        val password = pref.getString("password", "").orEmpty()
        val confirmPassword =
            pref.getString("confirmPassword", "").orEmpty()

        // Periksa kelengkapan data
        if (
            nama.isBlank() ||
            email.isBlank() ||
            tanggalLahir.isBlank() ||
            jenisKelamin.isBlank() ||
            username.isBlank() ||
            password.isBlank() ||
            confirmPassword.isBlank()
        ) {
            Toast.makeText(
                this,
                "Data belum lengkap. Silakan kembali ke registrasi.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Periksa format email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(
                this,
                "Format email tidak valid",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Periksa kesesuaian password
        if (password != confirmPassword) {
            Toast.makeText(
                this,
                "Password dan konfirmasi tidak sama",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Registrasi berhasil
        pref.edit()
            .putBoolean("registrasiBerhasil", true)
            .putBoolean("isLogin", true)
            .putString("username", username)
            .apply()

        Toast.makeText(
            this,
            "Registrasi berhasil!",
            Toast.LENGTH_SHORT
        ).show()

        // Pindah ke Dashboard dan hapus halaman sebelumnya dari back stack
        val intent = Intent(this, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(intent)
        finish()
    }
}
