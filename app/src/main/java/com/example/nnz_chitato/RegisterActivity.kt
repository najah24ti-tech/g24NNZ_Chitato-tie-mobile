
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

        // Tampilkan kembali data jika sebelumnya sudah disimpan
        tampilkanDataTersimpan()

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

    private fun tampilkanDataTersimpan() {
        val pref = getSharedPreferences("user_pref", MODE_PRIVATE)

        binding.edtNama.setText(pref.getString("nama", ""))
        binding.edtEmail.setText(pref.getString("email", ""))
        binding.edtTanggalLahir.setText(
            pref.getString("tanggalLahir", "")
        )
        binding.edtUsername.setText(pref.getString("username", ""))
        binding.edtPassword.setText(pref.getString("password", ""))
        binding.edtConfirmPassword.setText(
            pref.getString("confirmPassword", "")
        )

        when (pref.getString("jenisKelamin", "")) {
            "Laki-laki" ->
                binding.rgJenisKelamin.check(R.id.rbLakiLaki)

            "Perempuan" ->
                binding.rgJenisKelamin.check(R.id.rbPerempuan)
        }
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

        // Validasi semua input wajib diisi
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

        // Validasi format email
        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email).matches()
        ) {
            binding.edtEmail.error = "Format email tidak valid"
            binding.edtEmail.requestFocus()
            return
        }

        // Validasi kesamaan password
        if (password != confirmPassword) {
            binding.edtConfirmPassword.error =
                "Password dan konfirmasi tidak sama"
            binding.edtConfirmPassword.requestFocus()
            return
        }

        // Simpan seluruh data registrasi
        val sharedPref = getSharedPreferences(
            "user_pref",
            MODE_PRIVATE
        )

        sharedPref.edit()
            .putString("nama", nama)
            .putString("email", email)
            .putString("tanggalLahir", tanggalLahir)
            .putString("jenisKelamin", jenisKelamin)
            .putString("username", username)
            .putString("password", password)
            .putString("confirmPassword", confirmPassword)
            .putBoolean("registrasiBerhasil", false)
            .apply()

        // Buka halaman verifikasi
        val intent = Intent(
            this,
            VerifikasiActivity::class.java
        )
        startActivity(intent)
    }
}
