
package com.example.nnz_chitato

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.nnz_chitato.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(
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

        // Toolbar
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.title = "Home"

        // Username
        val sharedPref = requireContext().getSharedPreferences(
            "user_pref",
            Context.MODE_PRIVATE
        )

        val username = sharedPref.getString(
            "username",
            "Pengguna"
        ) ?: "Pengguna"

        binding.txtWelcome.text = "Hi, $username!"

        // Spinner bulan
        val months = listOf(
            "Januari",
            "Februari",
            "Maret",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Agustus",
            "September",
            "Oktober",
            "November",
            "Desember"
        )

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            months
        )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerMonth.adapter = spinnerAdapter

        // Quick Menu
        val menuList = listOf(
            HomeMenu("Input Penjualan", "📝"),
            HomeMenu("Detail Penjualan", "📊")
        )

        // CustomAdapter
        val menuAdapter = HomeMenuAdapter(
            requireContext(),
            menuList
        )

        binding.gridHomeMenu.adapter = menuAdapter

        // Klik Quick Menu
        binding.gridHomeMenu.setOnItemClickListener {
                _, _, position, _ ->

            when (position) {
                0 -> {
                    startActivity(
                        Intent(
                            requireContext(),
                            InputSalesActivity::class.java
                        )
                    )
                }

                1 -> {
                    startActivity(
                        Intent(
                            requireContext(),
                            DetailSalesActivity::class.java
                        )
                    )
                }
            }
        }

        // Grafik dibuat oleh SalesChartView
        // Tidak memerlukan pemanggilan binding tambahan.
    }

    override fun onDestroyView() {
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(null)

        _binding = null
        super.onDestroyView()
    }
}
