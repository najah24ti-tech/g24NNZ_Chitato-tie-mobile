
package com.example.nnz_chitato

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.nnz_chitato.databinding.ActivityProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    private val userPrefs by lazy {
        requireContext().getSharedPreferences(
            "user_pref",
            Context.MODE_PRIVATE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityProfileBinding.inflate(
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

        loadProfile()
        updateThemeButton()

        binding.btnDarkMode.setOnClickListener {
            ThemeManager.toggleTheme(requireActivity())
        }

        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        binding.btnDeveloperProfile.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    DeveloperProfileActivity::class.java
                )
            )
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    // Mengambil data langsung dari registrasi
    private fun loadProfile() {
        val username = userPrefs.getString(
            "username",
            "Pengguna"
        ) ?: "Pengguna"

        val nama = userPrefs.getString(
            "nama",
            username
        ) ?: username

        val email = userPrefs.getString(
            "email",
            ""
        ) ?: ""

        val wilayah = userPrefs.getString(
            "wilayah",
            ""
        ) ?: ""

        binding.txtProfileUsername.text = username
        binding.txtProfileName.text = nama

        binding.txtProfileEmail.text =
            if (email.isBlank()) "Belum diatur" else email

    }

    // Dialog Edit Profile
    private fun showEditProfileDialog() {
        val context = requireContext()

        val currentName = userPrefs.getString(
            "nama",
            ""
        ) ?: ""

        val currentEmail = userPrefs.getString(
            "email",
            ""
        ) ?: ""

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 16, 48, 8)
        }

        val nameInput = EditText(context).apply {
            hint = "Masukkan nama lengkap"
            setText(currentName)
            isSingleLine = true
        }

        val emailInput = EditText(context).apply {
            hint = "Masukkan email"
            setText(currentEmail)
            isSingleLine = true
            inputType = InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }

        container.addView(nameInput)
        container.addView(emailInput)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Edit Profile")
            .setMessage("Ubah informasi profil akun kamu.")
            .setView(container)
            .setPositiveButton("Simpan", null)
            .setNegativeButton("Batal", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener {
                    val newName =
                        nameInput.text.toString().trim()

                    val newEmail =
                        emailInput.text.toString().trim()


                    if (newName.isBlank()) {
                        nameInput.error =
                            "Nama tidak boleh kosong"
                        return@setOnClickListener
                    }

                    if (
                        newEmail.isNotBlank() &&
                        !android.util.Patterns.EMAIL_ADDRESS
                            .matcher(newEmail)
                            .matches()
                    ) {
                        emailInput.error =
                            "Format email tidak valid"
                        return@setOnClickListener
                    }

                    val saved = userPrefs.edit()
                        .putString("nama", newName)
                        .putString("email", newEmail)
                        .commit()

                    if (saved) {
                        loadProfile()

                        Toast.makeText(
                            context,
                            "Profil berhasil diperbarui",
                            Toast.LENGTH_SHORT
                        ).show()

                        dialog.dismiss()
                    } else {
                        Toast.makeText(
                            context,
                            "Gagal menyimpan profil",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        dialog.show()
    }

    // Konfirmasi Logout
    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah kamu yakin ingin logout?")
            .setPositiveButton("Ya") { _, _ ->

                // Hanya mengubah status login.
                // Data registrasi tetap tersimpan.
                userPrefs.edit()
                    .putBoolean("isLogin", false)
                    .apply()

                val intent = Intent(
                    requireContext(),
                    AuthActivity::class.java
                )

                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    // Mengubah teks tombol Dark Mode
    private fun updateThemeButton() {
        binding.btnDarkMode.text =
            if (ThemeManager.isDarkMode(requireContext())) {
                "Aktifkan Mode Terang"
            } else {
                "Aktifkan Mode Gelap"
            }
    }

    override fun onResume() {
        super.onResume()

        if (_binding != null) {
            loadProfile()
            updateThemeButton()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
