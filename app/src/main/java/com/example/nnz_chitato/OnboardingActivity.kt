
package com.example.nnz_chitato

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: Button
    private lateinit var btnSkip: TextView
    private lateinit var txtPageIndicator: TextView

    private val onboardingItems = listOf(
        OnboardingItem(
            "Welcome to HEBEPKU",
            "Aplikasi untuk membantu mengelola data penjualan dengan lebih mudah.",
            "📊"
        ),
        OnboardingItem(
            "Manage Your Sales",
            "Catat transaksi penjualan dan kelola produk dalam satu aplikasi.",
            "🛍️"
        ),
        OnboardingItem(
            "Track Your Business",
            "Pantau informasi penjualan dan perkembangan bisnis dengan praktis.",
            "📈"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPagerOnboarding)
        btnNext = findViewById(R.id.btnNext)
        btnSkip = findViewById(R.id.btnSkip)
        txtPageIndicator = findViewById(R.id.txtPageIndicator)

        viewPager.adapter = OnboardingAdapter(onboardingItems)

        updatePageIndicator(0)

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updatePageIndicator(position)
                }
            }
        )

        btnNext.setOnClickListener {
            val currentPage = viewPager.currentItem

            if (currentPage < onboardingItems.size - 1) {
                viewPager.currentItem = currentPage + 1
            } else {
                finishOnboarding()
            }
        }

        btnSkip.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun updatePageIndicator(position: Int) {
        txtPageIndicator.text =
            "${position + 1} / ${onboardingItems.size}"

        btnNext.text =
            if (position == onboardingItems.size - 1) {
                "Get Started"
            } else {
                "Next"
            }
    }

    private fun finishOnboarding() {

        // Simpan status onboarding sudah selesai
        val saved = getSharedPreferences(
            "user_pref",
            MODE_PRIVATE
        ).edit()
            .putBoolean("onboarding_completed", true)
            .commit()

        if (!saved) return

        // Arahkan pengguna ke halaman Login
        val intent = Intent(
            this,
            LoginActivity::class.java
        )

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }
}
