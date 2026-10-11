package com.example.nnz_chitato

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nnz_chitato.DashboardActivity
import com.example.nnz_chitato.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val user = binding.edtUsername.text.toString().trim()
            val pass = binding.edtPassword.text.toString()

            if (user == "najah" && pass == "12345") {
                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("username", user)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Username atau password salah",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}