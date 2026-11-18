
package com.example.nnz_chitato

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.widget.doAfterTextChanged
import com.example.nnz_chitato.databinding.ActivitySalesBinding

class SalesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalesBinding
    private lateinit var productAdapter: ProductAdapter

    private val allProducts = listOf(
        Product("Serum", "Skincare", "Rp85.000", "🧴"),
        Product("Sunscreen", "Skincare", "Rp65.000", "☀️"),
        Product("Handbody", "Bodycare", "Rp45.000", "🧴"),
        Product("Toner", "Skincare", "Rp55.000", "💧"),
        Product("Lipstick", "Makeup", "Rp75.000", "💄")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol kembali
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Sembunyikan navigasi bawah lama
        binding.bottomNavigation.visibility = View.GONE

        // Filter tanggal
        val dateAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("Today", "This Week", "This Month")
        )

        dateAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spFilterDate.adapter = dateAdapter

        // Filter kategori
        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf(
                "All Category",
                "Skincare",
                "Bodycare",
                "Makeup"
            )
        )

        categoryAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spFilterCategory.adapter = categoryAdapter

        // RecyclerView
        productAdapter = ProductAdapter(allProducts)

        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(this@SalesActivity)
            adapter = productAdapter
            isNestedScrollingEnabled = false
        }

        // Fungsi pencarian dan filter kategori
        fun filterProducts() {

            val keyword = binding.etSearchProduct.text
                .toString()
                .trim()

            val category = binding.spFilterCategory
                .selectedItem?.toString() ?: "All Category"

            val filteredProducts = allProducts.filter { product ->

                val matchName = product.name.contains(
                    keyword,
                    ignoreCase = true
                )

                val matchCategory =
                    category == "All Category" ||
                            product.category.equals(
                                category,
                                ignoreCase = true
                            )

                matchName && matchCategory
            }

            productAdapter.updateData(filteredProducts)
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
                    // Filter tanggal akan dihubungkan
                    // setelah data transaksi tersedia.
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
            }
    }
}
