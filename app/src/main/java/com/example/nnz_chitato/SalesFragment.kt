
package com.example.nnz_chitato

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nnz_chitato.databinding.ActivitySalesBinding
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SalesFragment : Fragment() {

    private var _binding: ActivitySalesBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter

    private val rupiah = NumberFormat.getCurrencyInstance(
        Locale("id", "ID")
    )

    private val dateFormat = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale("id", "ID")
    ).apply {
        isLenient = false
    }

    private val catalogProducts = listOf(
        Product("Serum", "Skincare", "", "🧴"),
        Product("Sunscreen", "Skincare", "", "☀️"),
        Product("Handbody", "Bodycare", "", "🧴"),
        Product("Toner", "Skincare", "", "💧"),
        Product("Lipstick", "Makeup", "", "💄"),
        Product("Conditioner", "Haircare", "", "🧴")
    )

    private var currentKeyword = ""
    private var currentCategory = "All Category"
    private var currentDateFilter = "All Dates"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ActivitySalesBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Sembunyikan navigasi lama
        binding.bottomNavigation.visibility = View.GONE
        binding.btnBack.visibility = View.GONE

        // Filter tanggal
        val dateOptions = listOf(
            "All Dates",
            "Today",
            "This Week",
            "This Month"
        )

        val dateAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            dateOptions
        )

        dateAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spFilterDate.adapter = dateAdapter
        binding.spFilterDate.setSelection(0, false)

        // Filter kategori
        val categoryOptions = listOf(
            "All Category",
            "Skincare",
            "Bodycare",
            "Makeup",
            "Haircare"
        )

        val categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categoryOptions
        )

        categoryAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spFilterCategory.adapter = categoryAdapter
        binding.spFilterCategory.setSelection(0, false)

        // RecyclerView
        productAdapter = ProductAdapter(emptyList())

        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
            isNestedScrollingEnabled = false
        }

        // Pencarian produk
        binding.etSearchProduct.doAfterTextChanged {
            currentKeyword = it.toString().trim()
            refreshSalesData()
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
                    currentCategory =
                        parent?.getItemAtPosition(position)?.toString()
                            ?: "All Category"

                    refreshSalesData()
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {}
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
                    currentDateFilter =
                        parent?.getItemAtPosition(position)?.toString()
                            ?: "All Dates"

                    refreshSalesData()
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {}
            }

        refreshSalesData()
    }

    override fun onResume() {
        super.onResume()

        if (_binding != null) {
            refreshSalesData()
        }
    }

    // Memuat ulang transaksi dan tampilan Sales
    private fun refreshSalesData() {

        val transactions = getTransactions()

        val filteredTransactions = transactions.filter {
            isWithinSelectedDate(it.optString("date"))
        }

        updateCategorySummary(filteredTransactions)
        updateProductList(filteredTransactions)
    }

    // Membaca transaksi dari SharedPreferences
    private fun getTransactions(): List<JSONObject> {

        val preferences = requireContext().getSharedPreferences(
            "sales_data",
            Context.MODE_PRIVATE
        )

        val savedData = preferences.getString(
            "transactions",
            "[]"
        ) ?: "[]"

        return try {

            val array = JSONArray(savedData)
            val result = mutableListOf<JSONObject>()

            for (i in 0 until array.length()) {
                array.optJSONObject(i)?.let {
                    result.add(it)
                }
            }

            result

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Memeriksa filter tanggal
    private fun isWithinSelectedDate(
        dateString: String
    ): Boolean {

        // Tampilkan semua transaksi tanpa batas tanggal
        if (currentDateFilter == "All Dates") {
            return true
        }

        val transactionDate = try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        } ?: return false

        val today = Calendar.getInstance()

        val transaction = Calendar.getInstance().apply {
            time = transactionDate
        }

        return when (currentDateFilter) {

            "Today" -> {
                today.get(Calendar.YEAR) ==
                        transaction.get(Calendar.YEAR) &&
                        today.get(Calendar.DAY_OF_YEAR) ==
                        transaction.get(Calendar.DAY_OF_YEAR)
            }

            "This Week" -> {

                val startOfWeek = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val endOfWeek = startOfWeek.clone() as Calendar
                endOfWeek.add(Calendar.DAY_OF_YEAR, 7)

                !transaction.before(startOfWeek) &&
                        transaction.before(endOfWeek)
            }

            "This Month" -> {
                today.get(Calendar.YEAR) ==
                        transaction.get(Calendar.YEAR) &&
                        today.get(Calendar.MONTH) ==
                        transaction.get(Calendar.MONTH)
            }

            else -> true
        }
    }

    // Memperbarui daftar produk
    private fun updateProductList(
        transactions: List<JSONObject>
    ) {

        val productSales =
            mutableMapOf<String, Pair<Int, Long>>()

        for (transaction in transactions) {

            val productName = transaction.optString("product")
            val quantity = transaction.optInt("quantity", 0)
            val subtotal = transaction.optLong("subtotal", 0L)

            val previous = productSales[productName]
                ?: Pair(0, 0L)

            productSales[productName] = Pair(
                previous.first + quantity,
                previous.second + subtotal
            )
        }

        val updatedProducts = catalogProducts.map { product ->

            val sales = productSales[product.name]
                ?: Pair(0, 0L)

            val salesInformation = """
                Terjual: ${sales.first} unit
                Omzet: ${rupiah.format(sales.second)}
            """.trimIndent()

            Product(
                product.name,
                product.category,
                salesInformation,
                product.icon
            )
        }

        // Filter pencarian dan kategori
        val filteredProducts = updatedProducts.filter { product ->

            val matchName = product.name.contains(
                currentKeyword,
                ignoreCase = true
            )

            val matchCategory =
                currentCategory == "All Category" ||
                        product.category.equals(
                            currentCategory,
                            ignoreCase = true
                        )

            matchName && matchCategory
        }

        productAdapter.updateData(filteredProducts)
    }

    // Menghitung ringkasan kategori
    private fun updateCategorySummary(
        transactions: List<JSONObject>
    ) {

        var skincareUnits = 0
        var makeupUnits = 0
        var bodycareUnits = 0
        var haircareUnits = 0

        var skincareRevenue = 0L
        var makeupRevenue = 0L
        var bodycareRevenue = 0L
        var haircareRevenue = 0L

        for (transaction in transactions) {

            val category = transaction.optString("category")
            val quantity = transaction.optInt("quantity", 0)
            val subtotal = transaction.optLong("subtotal", 0L)

            when (category) {

                "Skincare" -> {
                    skincareUnits += quantity
                    skincareRevenue += subtotal
                }

                "Makeup" -> {
                    makeupUnits += quantity
                    makeupRevenue += subtotal
                }

                "Bodycare" -> {
                    bodycareUnits += quantity
                    bodycareRevenue += subtotal
                }

                "Haircare" -> {
                    haircareUnits += quantity
                    haircareRevenue += subtotal
                }
            }
        }

        binding.txtSkincareUnits.text =
            "$skincareUnits unit"

        binding.txtSkincareRevenue.text =
            rupiah.format(skincareRevenue)

        binding.txtMakeupUnits.text =
            "$makeupUnits unit"

        binding.txtMakeupRevenue.text =
            rupiah.format(makeupRevenue)

        binding.txtBodycareUnits.text =
            "$bodycareUnits unit"

        binding.txtBodycareRevenue.text =
            rupiah.format(bodycareRevenue)

        binding.txtHaircareUnits.text =
            "$haircareUnits unit"

        binding.txtHaircareRevenue.text =
            rupiah.format(haircareRevenue)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
