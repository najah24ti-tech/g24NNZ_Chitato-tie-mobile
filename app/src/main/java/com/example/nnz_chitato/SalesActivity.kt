package com.example.nnz_chitato

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.nnz_chitato.databinding.ActivitySalesBinding

class SalesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol kembali
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Filter tanggal
        val dateAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            listOf("Today", "This Week", "This Month")
        )

        dateAdapter.setDropDownViewResource(
            R.layout.simple_spinner_dropdown_item
        )

        binding.spFilterDate.adapter = dateAdapter

        // Filter kategori
        val categoryAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            listOf("All Category", "Skincare", "Bodycare", "Makeup")
        )

        categoryAdapter.setDropDownViewResource(
            R.layout.simple_spinner_dropdown_item
        )

        binding.spFilterCategory.adapter = categoryAdapter

        // Daftar produk
        val products = listOf(
            binding.productSerum to "Serum Skincare",
            binding.productSunscreen to "Sunscreen Skincare",
            binding.productHandbody to "Handbody Bodycare",
            binding.productToner to "Toner Skincare",
            binding.productLipstick to "Lipstick Makeup"
        )

        // Fungsi pencarian dan filter kategori
        fun filterProducts() {
            val keyword = binding.etSearchProduct.text
                .toString()
                .trim()

            val category =
                binding.spFilterCategory.selectedItem?.toString()
                    ?: "All Category"

            products.forEach { (card, name) ->
                val matchName = name.contains(
                    keyword,
                    ignoreCase = true
                )

                val matchCategory =
                    category == "All Category" ||
                            name.contains(category, ignoreCase = true)

                card.visibility =
                    if (matchName && matchCategory) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
        }

        // Pencarian produk
        binding.etSearchProduct.doAfterTextChanged {
            filterProducts()
        }

        // Filter kategori
        binding.spFilterCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    filterProducts()
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                    // Tidak ada kategori dipilih
                }
            }

        // Filter tanggal
        binding.spFilterDate.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Pilihan tanggal tersedia.
                    // Penyaringan berdasarkan tanggal transaksi
                    // belum diterapkan karena data transaksi
                    // belum dihubungkan ke halaman ini.
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                    // Tidak ada filter tanggal dipilih
                }
            }

        // Navigasi Home / Dashboard
        binding.navHome.setOnClickListener {
            val intent = Intent(
                this,
                DashboardActivity::class.java
            )

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP

            startActivity(intent)
            finish()
        }

        // Navigasi Sales
        binding.navSales.setOnClickListener {
            // Tetap di halaman Sales
        }

        // Navigasi Input
        binding.navInput.setOnClickListener {
            startActivity(
                Intent(this, InputSalesActivity::class.java)
            )
            finish()
        }

        // Navigasi Detail
        binding.navDetail.setOnClickListener {
            startActivity(
                Intent(this, DetailSalesActivity::class.java)
            )
            finish()
        }

        // Navigasi Profile
        binding.navProfile.setOnClickListener {
            startActivity(
                Intent(this, ProfileActivity::class.java)
            )
            finish()
        }
    }
}
