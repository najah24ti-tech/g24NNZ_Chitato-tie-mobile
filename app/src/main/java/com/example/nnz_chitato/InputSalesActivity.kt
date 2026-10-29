package com.example.nnz_chitato

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.nnz_chitato.databinding.ActivityInputSalesBinding
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class InputSalesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputSalesBinding

    private val rupiah = NumberFormat.getNumberInstance(
        Locale("id", "ID")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInputSalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol kembali
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Dropdown wilayah
        val regions = listOf(
            "Pilih Wilayah",
            "Pekanbaru",
            "Batam",
            "Bengkulu",
            "Bukittinggi",
            "Padang"
        )

        binding.spRegion.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            regions
        ).also {
            it.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )
        }

        // Dropdown produk
        val products = listOf(
            "Pilih Produk",
            "Serum",
            "Sunscreen",
            "Handbody",
            "Toner",
            "Lipstick"
        )

        binding.spProduct.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            products
        ).also {
            it.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )
        }

        // Memilih tanggal melalui kalender
        binding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            DatePickerDialog(
                this,
                { _, year, month, day ->
                    binding.etDate.setText(
                        "%02d/%02d/%04d".format(
                            day,
                            month + 1,
                            year
                        )
                    )
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Menghitung total penjualan secara otomatis
        fun updateSubtotal() {
            val quantity = binding.etQuantity.text
                .toString()
                .toIntOrNull() ?: 0

            val price = binding.etPrice.text
                .toString()
                .toLongOrNull() ?: 0L

            val subtotal = quantity * price

            binding.tvSubtotal.text =
                "Total Penjualan: Rp${rupiah.format(subtotal)}"
        }

        binding.etQuantity.doAfterTextChanged {
            updateSubtotal()
        }

        binding.etPrice.doAfterTextChanged {
            updateSubtotal()
        }

        // Simpan penjualan dan buka Detail
        binding.btnSave.setOnClickListener {
            val date = binding.etDate.text.toString().trim()
            val ba = binding.etBA.text.toString().trim()
            val region = binding.spRegion.selectedItem.toString()
            val product = binding.spProduct.selectedItem.toString()

            val quantity = binding.etQuantity.text
                .toString()
                .toIntOrNull() ?: 0

            val price = binding.etPrice.text
                .toString()
                .toLongOrNull() ?: 0L

            // Memastikan data sudah diisi
            if (
                date.isBlank() ||
                ba.isBlank() ||
                quantity <= 0 ||
                price <= 0 ||
                region == "Pilih Wilayah" ||
                product == "Pilih Produk"
            ) {
                Toast.makeText(
                    this,
                    "Lengkapi semua data dengan benar",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val subtotal = quantity * price

            val detailIntent = Intent(
                this,
                DetailSalesActivity::class.java
            ).apply {
                putExtra("date", date)
                putExtra("ba", ba)
                putExtra("region", region)
                putExtra("product", product)
                putExtra("quantity", quantity)
                putExtra("price", price)
                putExtra("subtotal", subtotal)
            }

            startActivity(detailIntent)
        }

        // Navigasi Home / Dashboard
        binding.navHome.setOnClickListener {
            val homeIntent = Intent(
                this,
                DashboardActivity::class.java
            ).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

            startActivity(homeIntent)
        }

        // Navigasi Sales
        binding.navSales.setOnClickListener {
            startActivity(
                Intent(this, SalesActivity::class.java)
            )
        }

        // Navigasi Input: tetap di halaman ini
        binding.navInput.setOnClickListener {
            // Tidak perlu berpindah halaman
        }

        // Navigasi Detail
        binding.navDetail.setOnClickListener {
            startActivity(
                Intent(this, DetailSalesActivity::class.java)
            )
        }

        // Navigasi Profile
        binding.navProfile.setOnClickListener {
            startActivity(
                Intent(this, ProfileActivity::class.java)
            )
        }
    }
}
