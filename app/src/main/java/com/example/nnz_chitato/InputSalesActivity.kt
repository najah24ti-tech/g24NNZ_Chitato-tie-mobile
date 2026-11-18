
package com.example.nnz_chitato

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.nnz_chitato.databinding.ActivityInputSalesBinding
import org.json.JSONArray
import org.json.JSONObject
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
            "Lipstick",
            "Conditioner"
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

        // Memilih tanggal
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

        // Menghitung subtotal otomatis
        fun updateSubtotal() {
            val quantity = binding.etQuantity.text.toString().toIntOrNull() ?: 0
            val price = binding.etPrice.text.toString().toLongOrNull() ?: 0L
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

        // Tombol simpan
        binding.btnSave.setOnClickListener {

            val date = binding.etDate.text.toString().trim()
            val ba = binding.etBA.text.toString().trim()
            val region = binding.spRegion.selectedItem.toString()
            val product = binding.spProduct.selectedItem.toString()

            val quantity = binding.etQuantity.text.toString()
                .toIntOrNull() ?: 0

            val price = binding.etPrice.text.toString()
                .toLongOrNull() ?: 0L

            // Validasi
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

            // Cegah overflow perhitungan
            val subtotal = try {
                Math.multiplyExact(quantity.toLong(), price)
            } catch (e: ArithmeticException) {
                Toast.makeText(
                    this,
                    "Total penjualan terlalu besar",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val category = when (product) {
                "Serum", "Sunscreen", "Toner" -> "Skincare"
                "Handbody" -> "Bodycare"
                "Lipstick" -> "Makeup"
                "Conditioner" -> "Haircare"
                else -> "Lainnya"
            }

            // Simpan transaksi terlebih dahulu
            val saved = saveTransaction(
                date,
                ba,
                region,
                product,
                category,
                quantity,
                price,
                subtotal
            )

            if (!saved) {
                Toast.makeText(
                    this,
                    "Gagal menyimpan transaksi",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Penjualan berhasil disimpan",
                Toast.LENGTH_SHORT
            ).show()

            // Kirim data ke halaman Detail
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
    }

    // Menyimpan transaksi ke SharedPreferences
    private fun saveTransaction(
        date: String,
        ba: String,
        region: String,
        product: String,
        category: String,
        quantity: Int,
        price: Long,
        subtotal: Long
    ): Boolean {

        return try {
            val preferences = getSharedPreferences(
                "sales_data",
                MODE_PRIVATE
            )

            val oldData = preferences.getString(
                "transactions",
                "[]"
            )

            val transactions = JSONArray(oldData)

            val transaction = JSONObject().apply {
                put("date", date)
                put("ba", ba)
                put("region", region)
                put("product", product)
                put("category", category)
                put("quantity", quantity)
                put("price", price)
                put("subtotal", subtotal)
            }

            transactions.put(transaction)

            // commit memastikan hasil penyimpanan diketahui
            preferences.edit()
                .putString("transactions", transactions.toString())
                .commit()

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
