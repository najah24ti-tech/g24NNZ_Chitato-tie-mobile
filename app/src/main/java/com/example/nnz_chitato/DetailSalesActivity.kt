
package com.example.nnz_chitato

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nnz_chitato.databinding.ActivityDetailSalesBinding
import java.text.NumberFormat
import java.util.Locale

class DetailSalesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSalesBinding

    private val rupiah = NumberFormat.getCurrencyInstance(
        Locale("id", "ID")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailSalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengambil data transaksi
        val date = intent.getStringExtra("date") ?: "-"
        val ba = intent.getStringExtra("ba") ?: "-"
        val region = intent.getStringExtra("region") ?: "-"
        val product = intent.getStringExtra("product") ?: "-"

        val quantity = intent.getIntExtra("quantity", 0)
        val price = intent.getLongExtra("price", 0L)
        val subtotal = intent.getLongExtra("subtotal", 0L)

        // Menampilkan data transaksi
        binding.tvDate.text = date
        binding.tvBA.text = ba
        binding.tvRegion.text = region
        binding.tvProduct.text = product
        binding.tvQuantity.text = quantity.toString()
        binding.tvPrice.text = rupiah.format(price)
        binding.tvSubtotal.text = "Total Penjualan: ${rupiah.format(subtotal)}"

        // Tombol kembali
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Bagikan bukti transaksi
        binding.btnShareTransaction.setOnClickListener {
            val transaction = """
                BUKTI TRANSAKSI PENJUALAN
                ==========================
                Tanggal: $date
                Nama BA: $ba
                Wilayah: $region
                Produk: $product
                Jumlah: $quantity
                Harga per Produk: ${rupiah.format(price)}
                Total Penjualan: ${rupiah.format(subtotal)}

                Terima kasih.
            """.trimIndent()

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    "Bukti Transaksi Penjualan"
                )
                putExtra(Intent.EXTRA_TEXT, transaction)
            }

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Bagikan Bukti Transaksi"
                )
            )
        }

        // Navigasi Dashboard
        binding.navHome.setOnClickListener {
            val dashboardIntent = Intent(
                this,
                DashboardActivity::class.java
            ).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

            startActivity(dashboardIntent)
            finish()
        }

        // Navigasi Sales
        binding.navSales.setOnClickListener {
            startActivity(Intent(this, SalesActivity::class.java))
            finish()
        }

        // Navigasi Input Penjualan
        binding.navInput.setOnClickListener {
            startActivity(
                Intent(this, InputSalesActivity::class.java)
            )
            finish()
        }

        // Navigasi Detail
        binding.navDetail.setOnClickListener {
            // Tetap di halaman Detail Penjualan
        }

        // Navigasi Profil
        binding.navProfile.setOnClickListener {
            startActivity(
                Intent(this, ProfileActivity::class.java)
            )
            finish()
        }
    }
}
