
package com.example.nnz_chitato

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nnz_chitato.databinding.ActivityRegisterBinding
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Form registrasi selalu kosong saat dibuka
        kosongkanFormRegistrasi()

        // DatePicker untuk tanggal lahir
        binding.edtTanggalLahir.setOnClickListener {

            val calendar = Calendar.getInstance()

            DatePickerDialog(
                this,
                { _, year, month, day ->

                    binding.edtTanggalLahir.setText(
                        String.format(
                            Locale.getDefault(),
                            "%02d/%02d/%04d",
                            day,
                            month + 1,
                            year
                        )
                    )
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.maxDate = System.currentTimeMillis()
            }.show()
        }

        // Tombol Selanjutnya
        binding.btnSelanjutnya.setOnClickListener {
            simpanDanLanjutkan()
        }
    }

    // Mengosongkan formulir tanpa menghapus data akun lama
    private fun kosongkanFormRegistrasi() {

        binding.edtNama.text?.clear()
        binding.edtEmail.text?.clear()
        binding.edtTanggalLahir.text?.clear()
        binding.edtUsername.text?.clear()
        binding.edtPassword.text?.clear()
        binding.edtConfirmPassword.text?.clear()

        binding.rgJenisKelamin.clearCheck()
    }

    private fun simpanDanLanjutkan() {

        val nama = binding.edtNama.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val tanggalLahir =
            binding.edtTanggalLahir.text.toString().trim()
        val username = binding.edtUsername.text.toString().trim()
        val password = binding.edtPassword.text.toString()
        val confirmPassword =
            binding.edtConfirmPassword.text.toString()

        val jenisKelamin = when (
            binding.rgJenisKelamin.checkedRadioButtonId
        ) {
            R.id.rbLakiLaki -> "Laki-laki"
            R.id.rbPerempuan -> "Perempuan"
            else -> ""
        }

        // Validasi semua input
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
                "Semua data wajib diisi",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Validasi email
        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email).matches()
        ) {
            binding.edtEmail.error = "Format email tidak valid"
            binding.edtEmail.requestFocus()
            return
        }

        // Validasi password
        if (password != confirmPassword) {
            binding.edtConfirmPassword.error =
                "Password dan konfirmasi tidak sama"
            binding.edtConfirmPassword.requestFocus()
            return
        }

        // Simpan data registrasi
        val sharedPref = getSharedPreferences(
            "user_pref",
            MODE_PRIVATE
        )

        val berhasil = sharedPref.edit()
            .putString("nama", nama)
            .putString("email", email)
            .putString("tanggalLahir", tanggalLahir)
            .putString("jenisKelamin", jenisKelamin)
            .putString("username", username)
            .putString("password", password)
            .putString("confirmPassword", confirmPassword)
            .putBoolean("registrasiBerhasil", false)
            .putBoolean("isLogin", false)
            .commit()

        if (!berhasil) {
            Toast.makeText(
                this,
                "Data registrasi gagal disimpan",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Buka halaman verifikasi
        startActivity(
            Intent(this, VerifikasiActivity::class.java)
        )
    }
}
