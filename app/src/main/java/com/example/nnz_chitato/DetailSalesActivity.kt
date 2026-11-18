
package com.example.nnz_chitato

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nnz_chitato.databinding.ActivityDetailSalesBinding
import java.text.NumberFormat
import java.util.Locale

class DetailSalesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSalesBinding

    private val rupiah = NumberFormat.getNumberInstance(
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

        // Menampilkan detail transaksi
        binding.tvDate.text = date
        binding.tvBA.text = ba
        binding.tvRegion.text = region
        binding.tvProduct.text = product
        binding.tvQuantity.text = "$quantity unit"
        binding.tvPrice.text = "Rp${rupiah.format(price)}"
        binding.tvSubtotal.text = "Rp${rupiah.format(subtotal)}"

        // Kembali ke halaman sebelumnya
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Kembali ke Dashboard (HomeFragment)
        binding.btnDashboard.setOnClickListener {
            val dashboardIntent = Intent(
                this,
                BaseActivity::class.java
            ).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

            startActivity(dashboardIntent)
            finish()
        }

        // Membuka tab SalesFragment
        binding.btnSales.setOnClickListener {
            val salesIntent = Intent(
                this,
                BaseActivity::class.java
            ).apply {
                putExtra("open_sales", true)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

            startActivity(salesIntent)
            finish()
        }

        // Bagikan transaksi
        binding.btnShareTransaction.setOnClickListener {
            val receipt = """
                DETAIL PENJUALAN HEBEPKU

                Tanggal: $date
                BA: $ba
                Wilayah: $region
                Produk: $product
                Jumlah: $quantity unit
                Harga: Rp${rupiah.format(price)}

                Total Penjualan:
                Rp${rupiah.format(subtotal)}
            """.trimIndent()

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, receipt)
            }

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Bagikan Detail Penjualan"
                )
            )
        }
    }
}
