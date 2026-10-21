package com.example.nnz_chitato

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nnz_chitato.databinding.ActivityDeveloperProfileBinding

class DeveloperProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeveloperProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeveloperProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
