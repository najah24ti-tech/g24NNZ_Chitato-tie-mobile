package com.example.nnz_chitato

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nnz_chitato.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol menuju halaman detail Chitato
        binding.btnMyProject.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }

        // Tombol menuju halaman website Chitato
        binding.btnWebsite.setOnClickListener {
            val intent = Intent(this, ChitatoWebActivity::class.java)
            startActivity(intent)
        }
    }
}