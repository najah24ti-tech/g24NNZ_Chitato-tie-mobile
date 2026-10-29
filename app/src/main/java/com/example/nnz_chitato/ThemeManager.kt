
package com.example.nnz_chitato

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {

    private const val PREF_NAME = "chitato_settings"
    private const val KEY_DARK_MODE = "dark_mode"

    fun isDarkMode(context: Context): Boolean {
        val prefs = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getBoolean(KEY_DARK_MODE, false)
    }

    fun applySavedTheme(context: Context) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode(context)) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun toggleTheme(context: Context) {
        val newMode = !isDarkMode(context)

        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        ).edit()
            .putBoolean(KEY_DARK_MODE, newMode)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (newMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
