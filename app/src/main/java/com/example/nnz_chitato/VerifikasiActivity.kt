
package com.example.nnz_chitato

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nnz_chitato.databinding.ActivityVerifikasiBinding

class VerifikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifikasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tampilkanDataRegistrasi()

        binding.btnKembali.setOnClickListener {
            finish()
        }

        binding.btnSubmit.setOnClickListener {
            validasiData()
        }
    }

    private fun tampilkanDataRegistrasi() {
        val pref = getSharedPreferences("user_pref", MODE_PRIVATE)

        binding.txtNama.text = pref.getString("nama", "-") ?: "-"
        binding.txtEmail.text = pref.getString("email", "-") ?: "-"
        binding.txtTanggalLahir.text =
            pref.getString("tanggalLahir", "-") ?: "-"
        binding.txtJenisKelamin.text =
            pref.getString("jenisKelamin", "-") ?: "-"
        binding.txtUsername.text =
            pref.getString("username", "-") ?: "-"
        binding.txtPassword.text =
            pref.getString("password", "-") ?: ""
        binding.txtConfirmPassword.text =
            pref.getString("confirmPassword", "-") ?: ""
    }

    private fun validasiData() {
        val pref = getSharedPreferences("user_pref", MODE_PRIVATE)

        val nama = pref.getString("nama", "") ?: ""
        val email = pref.getString("email", "") ?: ""
        val username = pref.getString("username", "") ?: ""
        val password = pref.getString("password", "") ?: ""
        val confirmPassword =
            pref.getString("confirmPassword", "") ?: ""

        if (nama.isBlank() || email.isBlank() ||
            username.isBlank() || password.isBlank()
        ) {
            Toast.makeText(
                this,
                "Data registrasi belum lengkap",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(
                this,
                "Format email tidak valid",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(
                this,
                "Password dan konfirmasi password tidak sama",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val berhasil = pref.edit()
            .putBoolean("registrasiBerhasil", true)
            .putBoolean("isLogin", false)
            .putString("username", username)
            .putString("nama", nama)
            .putString("email", email)
            .commit()

        if (!berhasil) {
            Toast.makeText(
                this,
                "Data gagal disimpan",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        Toast.makeText(
            this,
            "Registrasi berhasil! Silakan login.",
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
